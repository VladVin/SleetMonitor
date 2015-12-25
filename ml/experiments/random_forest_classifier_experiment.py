from sklearn.ensemble import RandomForestClassifier
from data_reader import DataReader

class RandomForestClassifierExperiment(object):
    def __init__(self, trees_count):
        self._clf = RandomForestClassifier(trees_count)

    def train(self, trainX, trainY):
        print 'Train data batch size: ', len(trainX)
        self._clf = self._clf.fit(trainX, trainY)

    def test(self, testX, testY):
        testDataSize = len(testX);
        print 'Test data batch size: ', testDataSize
        correctAnswers = 0
        res_y = self._clf.predict(testX)
        for i in range(0, len(testY)):
            if (res_y[i] == testY[i]):
                correctAnswers += 1
        accuracy = correctAnswers / testDataSize
        print 'Accuracy: ', accuracy * 100, '%'

if (__name__ == '__main__'):

    dataReader = DataReader()
    data, labels = dataReader.parse('../data/data1.txt')

    rfcExp = RandomForestClassifierExperiment(10)

    # Train and test classifier on different data
    trainBatchSize = int(0.8 * len(data))
    rfcExp.train(data[0:trainBatchSize], labels[0:trainBatchSize])
    rfcExp.test(data[trainBatchSize:], labels[trainBatchSize:])
