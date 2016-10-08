import numpy as np
import matplotlib.pyplot as plt
from scipy.interpolate import splev, splrep
from collections import OrderedDict

class Interpolator:
    @staticmethod
    def interpolate(data, start, end, step):
        ordered_data = OrderedDict(sorted(data.items()))
        x = np.array(ordered_data.keys())
        y = np.array(ordered_data.values())

        tck = splrep(x, y)
        new_x = np.linspace(start, end, end / step + 1)
        return splev(new_x, tck)

# data = {7:-0.8, 19:-0.4, 26:-0.1, 33:0.2, 41:0.44}
# interp_data = Interpolator.interpolate(data, 5, 50, 5)
# plt.plot(data.keys(), data.values(), 'o', np.linspace(5, 50, 11), interp_data)
# plt.show()