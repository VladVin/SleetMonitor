import re
import random

# Data represent as [frame_data], [label]
class DataReader(object):
    def parse(self, filename):
        data = []
        labels = []
        f = open(filename, 'r')
        for line in f:
            frame_data_str = re.findall(r"-?\d+\.\d*", line)
            frame_true_label_str = re.findall(r"true", line)
            frame_false_label_str = re.findall(r"false", line)
            frame_label = -1
            if (len(frame_true_label_str) != 0):
                frame_label = 1
            elif (len(frame_false_label_str) != 0):
                frame_label = 0

            if (len(frame_data_str) > 0 and frame_label != -1):
                frame_data = []
                for i in range(0, len(frame_data_str)):
                    frame_data.append(float(frame_data_str[i]))
                data.append(frame_data)
                labels.append(frame_label)
                
        f.close()
        return data, labels

    def shuffle(self, data, labels):
        length = min(len(data), len(labels))
        data_in = data[:]
        labels_in = labels[:]
        data_out = []
        labels_out = []
        for i in range(0, length):
            rnd_idx = random.randint(0, len(data_in) - 1)
            data_out.append(data_in.pop(rnd_idx))
            labels_out.append(labels_in.pop(rnd_idx))
        return data_out, labels_out
