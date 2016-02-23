from sklearn.ensemble import RandomForestClassifier
from data_reader import DataReader

class RandomForestClassifierExperiment(object):
    def __init__(self, trees_count):
        self._clf = RandomForestClassifier(trees_count)

    def train(self, train_x, train_y):
        print 'Train data batch size: ', len(train_x)
        self._clf = self._clf.fit(train_x, train_y)

    def test(self, test_x, test_y):
        testDataSize = len(test_x)
        print 'Test data batch size: ', testDataSize
        correctAnswers = 0
        res_y = self._clf.predict(test_x)
        for i in range(0, len(test_y)):
            if (res_y[i] == test_y[i]):
                correctAnswers += 1
        accuracy = float(correctAnswers) / float(testDataSize)
        print 'Accuracy: ', accuracy * 100.0, '%'

    def multiple_test(self, train_x, train_y, test_x, test_y, count_test=10):
        print 'Train data batch size: ', len(train_x)
        print 'Test data batch size: ', len(test_x)

        testDataSize = len(test_x)
        accuracy_list = []
        for i in range(0, count_test):
            self._clf = self._clf.fit(train_x, train_y)
            correctAnswers = 0
            res_y = self._clf.predict(test_x)
            for i in range(0, len(test_y)):
                if (res_y[i] == test_y[i]):
                    correctAnswers += 1
            accuracy = float(correctAnswers) / float(testDataSize)
            print 'Accuracy: ', accuracy * 100.0, '%'
            accuracy_list.append(accuracy)
        print 'Min accuracy: ', min(accuracy_list) * 100.0, '%'
        print 'Max accuracy: ', max(accuracy_list) * 100.0, '%'
        print 'Average accuracy: ', reduce(lambda x, y: x + y, accuracy_list) / len(accuracy_list) * 100.0, '%'

if (__name__ == '__main__'):

    dataReader = DataReader()
    data, labels = dataReader.parse('../data/data3_55_196.txt')

    data, labels = dataReader.shuffle(data, labels)

    train_batch_size = int(0.8 * len(data))

    rfcExp = RandomForestClassifierExperiment(10)

    # Train and test classifier on different data
    rfcExp.multiple_test(data[0:train_batch_size], labels[0:train_batch_size],
        data[train_batch_size:], labels[train_batch_size:])
    # rfcExp.train(data[0:train_batch_size], labels[0:train_batch_size])
    # rfcExp.test(data[train_batch_size:], labels[train_batch_size:])
