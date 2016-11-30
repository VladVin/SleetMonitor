import numpy as np
from scipy.interpolate import InterpolatedUnivariateSpline

class DataInterpolator:
    @staticmethod
    def interpolate(data, start, end, step, degree=1):
        new_data = np.ndarray((int((end - start + step) / step) + int((end - start) % step != 0), data.shape[1]),
                              dtype=np.dtype('double'))
        new_data[:, 0] = np.arange(start, end + step, step)

        print('Interpolation - data: ' + str(data.shape) + ' new_data: ' + str(new_data.shape))

        for i in range(1, data.shape[1]):
            spl = InterpolatedUnivariateSpline(data[:, 0], data[:, i], k=degree)
            new_data[:, i] = spl(new_data[:, 0])

        return new_data
