import numpy as np
import matplotlib.pyplot as plt
from scipy.interpolate import interp1d
from collections import OrderedDict
from scipy.interpolate import InterpolatedUnivariateSpline

class Interpolator:
    @staticmethod
    def interpolate(data, start, end, step, degree=1):
        new_data = []

        new_timestamps = np.arange(start, end + step, step)
        for frame_data in data:
            new_frame_data = []
            for k in xrange(0, len(frame_data)):
                new_frame_data.append([])
            new_frame_data[0].extend(new_timestamps)
            for i in xrange(1, len(frame_data)):
                spl = InterpolatedUnivariateSpline(frame_data[0], frame_data[i], k=degree)
                new_frame_data[i].extend(spl(new_timestamps))
            new_data.append(new_frame_data)

        return new_data

    @staticmethod
    def cut_data(data, start, end, sparseness):
        return Interpolator.interpolate(data, start, end, sparseness, degree=1)
