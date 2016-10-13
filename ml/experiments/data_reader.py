import re
import random

# Data represent as [frame_data], [label]
class DataReader(object):
    def __init__(self, xyz):
        self.__xyz = xyz

    def parse(self, filename):
        data = []
        labels = []
        f = open(filename, 'r')
        line_number = 0
        for line in f:
            line_number += 1
            frame_ts_str = re.findall(r"\d+:", line)
            frame_data_str = re.findall(r"-?\d+\.\d*E?-?\d+?", line)
            if (len(frame_ts_str) != len(frame_data_str) / 3):
                print 'Bad file structure!'
                print 'ts_size: %d data_size: %d' % (len(frame_ts_str), len(frame_data_str))

            frame_data = []
            for k in xrange(0, len(self.__xyz) + 1):
                frame_data.append([])
            for ts_idx in xrange(len(frame_ts_str)):
                frame_data[0].append(int(frame_ts_str[ts_idx][:-1]))
                for k in xrange(0, len(self.__xyz)):
                    frame_data[k + 1].append(float(frame_data_str[ts_idx * 3 + self.__xyz[k]]))
                    

            frame_true_label_str = re.findall(r"true", line)
            frame_false_label_str = re.findall(r"false", line)
            frame_label = -1
            if (len(frame_true_label_str) != 0):
                frame_label = 1
            elif (len(frame_false_label_str) != 0):
                frame_label = 0

            if (frame_label != -1):
                data.append(frame_data)
                labels.append(frame_label)
            else:
                print 'Bad label! Line number: %d' % (line_number)
                
        f.close()
        return data, labels

    def shuffle(self, data, labels):
        shuffled_idxs = range(0, len(data))
        random.shuffle(shuffled_idxs)
        new_data = []
        new_labels = []
        for idx in shuffled_idxs:
            new_data.append(data[idx])
            new_labels.append(labels[idx])

        return new_data, new_labels

    def change_data_view(self, data):
        new_data = []
        for data_frame in data:
            new_data_frame = []
            for i in xrange(0, len(data_frame[0])):  # timestamps
                for k in xrange(0, len(self.__xyz)):
                    new_data_frame.append(data_frame[k + 1][i])
            new_data.append(new_data_frame)

        return new_data
