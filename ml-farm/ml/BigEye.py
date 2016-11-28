import numpy as np
from storage.DataInterpolator import DataInterpolator

class BigEye(object):
    def __init__(self, dbm, oracol):
        self._dbm = dbm
        self._oracol = oracol

    def run(self):
        while True:
            user_sd = self._dbm.get_fresh_sensor_data()
            fall_statuses = {}

            for user_id, sdata in user_sd.items():
                data = np.flipud(sdata)
                tsps = data[:, 0].astype('int64')
                start = tsps[0]
                end = tsps[-1]

                idata = DataInterpolator.interpolate(data, start, end, self._oracol.step)
                itsps = idata[:, 0].astype('int64')
                frame_size = int(self._oracol.frame_duration / self._oracol.step)
                limit = len(itsps[itsps < itsps[-1] - self._oracol.frame_duration])

                test_data = np.empty(idata.shape[0] * (idata.shape[1] - 1))
                x_mask = [3 * i for i in range(idata.shape[0])]
                y_mask = [3 * i + 1 for i in range(idata.shape[0])]
                z_mask = [3 * i + 2 for i in range(idata.shape[0])]
                test_data[x_mask] = idata[:, 1]
                test_data[y_mask] = idata[:, 2]
                test_data[z_mask] = idata[:, 3]

                fall_status = np.empty((tsps.shape[0], 2), dtype=np.dtype('int64'))
                fall_status[:, 0] = tsps
                fall_status[:, 1] = -1

                pivot = 0

                for i in range(0, limit):
                    frame_data = test_data[3 * i:3*(i + frame_size)]
                    predicted = self._oracol.predict(np.expand_dims(frame_data, 0))

                    if itsps[i] > tsps[pivot] and (pivot + 1 < len(tsps)):
                        pivot += 1
                    if fall_status[pivot, 1] != 1:
                        fall_status[pivot, 1] = predicted

                fall_statuses[user_id] = fall_status

            self._dbm.update_fall_statuses(fall_statuses)
