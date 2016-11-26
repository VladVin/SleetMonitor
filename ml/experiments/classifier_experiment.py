import sys
import os.path

from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from data_reader import DataReader
from interpolator import Interpolator
import matplotlib.pyplot as plt
import numpy as np

class ClassifierExperiment(object):
    def __init__(self, clf):
        self._clf = clf;

    def train(self, train_x, train_y):
        self._clf = self._clf.fit(train_x, train_y)

    def test(self, test_x, test_y):
        testDataSize = len(test_x)
        correctAnswers = 0
        res_y = self._clf.predict(test_x)
        for i in xrange(0, len(test_y)):
            if (res_y[i] == test_y[i]):
                correctAnswers += 1
        accuracy = float(correctAnswers) / float(testDataSize)
        return accuracy

    def evaluate(self, train_x, train_y, test_x, test_y, test_count=10):
        if (type(self._clf) is not RandomForestClassifier):
            test_count = 1

        accuracy_list = []
        for i in xrange(0, test_count):
            self.train(train_x, train_y)
            accuracy = self.test(test_x, test_y)
            accuracy_list.append(accuracy)

        return max(accuracy_list)

if (__name__ == '__main__'):

    help_mes = "Incorrect arguments. Put data file name"
    if (len(sys.argv) < 2):
        print help_mes
        exit()
    fname = sys.argv[1]
    if (not os.path.exists(fname)):
        print help_mes
        exit()

    num_epoch = 10
    test_count_for_rfc = 10

    train_sparseness = 5
    train_frame_start = 5
    train_frame_end = 1500
    test_sparseness = 50
    test_frame_start = test_sparseness
    test_frame_end = 1500

    interpolation_degree = 1
    xyz = [0, 1, 2]     # x=0, y=1, z=2

    # params for RFC
    number_of_trees = 10

    # params for SVC
    kernel = 'linear'
    C = 1
    gamma = 10

    # Choose a classifier
    alg = RandomForestClassifier(number_of_trees)
    # alg = SVC(kernel=kernel, C=C, gamma=gamma)

    visualize_xyz_example = False
    visualize_interpolated = False
    training_enabled = True

    data_reader = DataReader(xyz)
    print "Parsing data..."
    data, labels = data_reader.parse(fname)

    if (visualize_xyz_example):
        timestamps = np.arange(train_frame_start,
            train_frame_end + train_sparseness, train_sparseness)
        visualize_count = 3
        visualized = 0
        for i in xrange(0, len(labels)):
            if (labels[i] and visualized < visualize_count):
                plt.figure(figsize=(20, 10))
                plt.plot(timestamps, data[i][1], 'r')
                plt.plot(timestamps, data[i][2], 'g')
                plt.plot(timestamps, data[i][3], 'b')
                plt.xlabel('frame time (msec)')
                plt.ylabel('acceleromenter value')
                plt.legend(['x', 'y', 'z'])
                plt.show()
                visualized += 1
            
    if (visualize_interpolated):
        timestamps = np.arange(train_frame_start,
            train_frame_end + train_sparseness, train_sparseness)

        visualize_count = 3
        visualized = 0
        for i in xrange(0, len(labels)):
            if (not labels[i] and visualized < visualize_count):
                cutted_data = Interpolator.cut_data([data[i]],
                    test_frame_start, test_frame_end, test_sparseness)
                interpolated_data = Interpolator.interpolate(
                    cutted_data, train_frame_start,
                    train_frame_end, train_sparseness,
                    interpolation_degree)
                plt.figure(figsize=(20, 10))
                plt.plot(timestamps, data[i][2], '--b')
                plt.plot(timestamps, interpolated_data[0][2], '-r')
                plt.xlabel('frame time (msec)')
                plt.ylabel('acceleromenter value')
                plt.legend(['source data', 'interpolated data'])
                plt.show()
                visualized += 1
        
    if (training_enabled):
        train_dataset_size = int(0.8 * len(data))

        print "Training and evaluating process started."
        print 'num epoches = %d' % (num_epoch)
        print 'test count = %d' % (test_count_for_rfc)
        print 'train frame params: %d %d %d' % (train_frame_start, train_frame_end, train_sparseness)
        print 'test frame params: %d %d %d' % (test_frame_start, test_frame_end, test_sparseness)
        print 'interpolation degree = %d' % (interpolation_degree)
        print 'accelerometer values: %s %s %s' % \
            ('x' if 0 in xyz else '-', 'y' if 1 in xyz else '-', 'z' if 2 in xyz else '-')
        print 'dataset size = %d, pos_num = %d, neg_num = %d' % \
            (len(data),
            sum(1 for label in labels if label),
            sum(1 for label in labels if not label))
        print 'train data batch size = %d' % (train_dataset_size)
        print 'test data batch size = %d' % (len(data) - train_dataset_size)
        if (type(alg) is RandomForestClassifier):
            print 'RFC params: number of trees = %d, test count = %d' % \
                (number_of_trees, test_count_for_rfc)
        elif (type(alg) is SVC):
            print 'SVC params: kernel = %s, C = %f, gamma = %f' % \
                (kernel, C, gamma)

        clf_exp = ClassifierExperiment(alg)
        accuracy_list = []
        for epoch in xrange(0, num_epoch):
            print 'Epoch %d' % (epoch)

            data, labels = data_reader.shuffle(data, labels)

            # Divide data into two parts: training and testing
            train_data = data[0:train_dataset_size]
            train_labels = labels[0:train_dataset_size]
            train_data = data_reader.change_data_view(train_data)
            
            test_data = data[train_dataset_size:]
            test_labels = labels[train_dataset_size:]
            test_data = Interpolator.cut_data(test_data,
                test_frame_start, test_frame_end, test_sparseness)
            test_data = Interpolator.interpolate(
                test_data, train_frame_start,
                train_frame_end, train_sparseness,
                interpolation_degree)

            test_data = data_reader.change_data_view(test_data)
            # Train and test classifier on different data
            cur_accuracy = clf_exp.evaluate(train_data, train_labels, test_data, test_labels, test_count_for_rfc)
            accuracy_list.append(cur_accuracy)
            print 'current accurancy:', cur_accuracy

        print 'MIN ACCURACY: ', min(accuracy_list) * 100.0, '%'
        print 'MAX ACCURACY: ', max(accuracy_list) * 100.0, '%'
        print 'AVERAGE ACCURACY: ', reduce(lambda x, y: x + y, accuracy_list) / len(accuracy_list) * 100.0, '%'
