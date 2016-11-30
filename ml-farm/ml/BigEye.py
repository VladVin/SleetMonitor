import numpy as np
from storage.DataInterpolator import DataInterpolator
import time
import traceback


class BigEye(object):
    def __init__(self, dbm, oracol):
        self._dbm = dbm
        self._oracol = oracol
        self._sleep_time = 0.010  # sec
        self._xyz = (1, 0, 1)
        self._max_avg_ts_diff = 100  # ms
        self._min_data_size = 100

    def run(self):
        print('Big eye is ready')

        while True:
            try:
                user_sd = self._dbm.get_fresh_sensor_data(self._xyz)
                fall_statuses = {}

                for user_id, sdata in user_sd.items():
                    if sdata.shape[0] < self._min_data_size:
                        self._dbm.set_not_updated_status([user_id])
                        continue

                    data = np.flipud(sdata)
                    tsps = data[:, 0].astype('int64')
                    start = tsps[0]
                    end = tsps[-1]

                    fall_status = np.empty((tsps.shape[0], 2), dtype=np.dtype('int64'))
                    fall_status[:, 0] = tsps
                    fall_status[:, 1] = 0

                    # Not realistic timestamp diff
                    if (end - start) / sdata.shape[0] > self._max_avg_ts_diff:
                        fall_status[:, 1] = -1
                        fall_statuses[user_id] = fall_status
                        self._dbm.set_not_updated_status([user_id])
                        continue

                    idata = DataInterpolator.interpolate(data, start, end, self._oracol.step)
                    itsps = idata[:, 0].astype('int64')
                    frame_size = int(self._oracol.frame_duration / self._oracol.step)
                    limit = len(itsps[itsps < itsps[-1] - self._oracol.frame_duration])

                    if limit < self._min_data_size:
                        self._dbm.set_not_updated_status([user_id])
                        continue

                    print('start: ' + str(start) + ' end: ' + str(end) + ' diff: ' + str(end - start))

                    axes_count = idata.shape[1] - 1
                    test_data = np.empty(idata.shape[0] * axes_count)

                    for a_idx in range(axes_count):
                        mask = [axes_count * i + a_idx for i in range(idata.shape[0])]
                        test_data[mask] = idata[:, a_idx + 1]

                    pivot = 0
                    pred = []
                    for i in range(0, limit):
                        frame_data = test_data[axes_count * i:axes_count * (i + frame_size)]
                        predicted = self._oracol.predict(np.expand_dims(frame_data, 0))

                        pred.append(predicted[0])

                        if itsps[i] > tsps[pivot] and (pivot + 1 < len(tsps)):
                            pivot += 1
                        # if fall_status[pivot, 1] != 1:
                        fall_status[pivot, 1] = predicted[0]

                    fall_statuses[user_id] = fall_status

                    pred = np.array(pred)
                    pos_count = len(pred[pred == 1])
                    pred_count = len(pred)
                    print(pos_count, pred_count)
                    # print(fall_status[0:fall_status.shape[0] - limit])
                    if pos_count > 0.5 * pred_count:
                        for i in range(10):
                            print('FALL FALL FALL FALL FALL FALL ' +
                                  'FALL FALL FALL FALL FALL FALL')
                        # BigEye.store_frame(test_data[0:axes_count * frame_size])

                self._dbm.update_fall_statuses(fall_statuses)

                time.sleep(self._sleep_time)
            except Exception:
                traceback.print_exc()

    @staticmethod
    def store_frame(data):
        frame_str = ''
        for i in range(int(data.shape[0] / 3)):
            t = i * 5 + 5
            x = data[i]
            y = data[i + 1]
            z = data[i + 2]
            frame_str += str(t) + ': ' + str(x) + ' ' + str(y) + ' ' + str(z) + ' '
        frame_str += 'false\n'

        f = open('../../ml/data/fighting_neg.txt', 'a+')
        f.write(frame_str)
        f.close()
