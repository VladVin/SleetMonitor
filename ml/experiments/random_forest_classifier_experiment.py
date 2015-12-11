from sklearn.ensemble import RandomForestClassifier
from data_reader import DataReader

class RandomForestClassifierExperiment(object):
    def __init__(self, trees_count):
        self._clf = RandomForestClassifier(trees_count)

    def train(self, train_x, train_y):
        self._clf = self._clf.fit(X, Y)

    def test(self, test_x, test_y):
        res = self._clf.predict(test_x)
        print res

if (__name__ == '__main__'):
    X = [[0, 1], [1, 1]]
    Y = [0, 1]

    data_reader = DataReader()
    data = data_reader.parse('../data/data.txt')
    print data
    # for i in range(0, len(data)):
        # print str(len(data[i][0])) + ' ' + str(data[i][1])

    # rfce = RandomForestClassifierExperiment(10)
    # rfce.train(X, Y)
    # rfce.test([[1, 0]], [])
