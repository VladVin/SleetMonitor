import re

class DataReader(object):
    def parse(self, filename):
        data = []
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
                data.append((frame_data, frame_label))
                
        f.close()
        return data
