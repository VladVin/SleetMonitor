from sklearn.externals import joblib

class Oracol(object):
    def __init__(self):
        self._model_fname = '../../ml/models/ice_clf_f1score_rnd_fight2_xz_2.pkl'
        self._model = joblib.load(self._model_fname)
        self.frame_duration = 1500
        self.step = 5

    def predict(self, data):
        return self._model.predict(data)
