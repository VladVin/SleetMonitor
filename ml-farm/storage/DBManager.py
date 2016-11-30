from cassandra.cluster import Cluster
import numpy as np

class DBManager(object):
    def __init__(self):
        self._keyspace_host = '127.0.0.1'
        self._keyspace = 'white_snow'
        self._cluster = Cluster([self._keyspace_host])
        self._session = self._cluster.connect(self._keyspace)

    def get_fresh_sensor_data(self, xyz):
        active_axes_count = len(np.where(np.array(xyz) == 1)[0])
        if active_axes_count == 0:
            return {}
        axis_q = ''
        if xyz[0] == 1:
            axis_q += 'x, '
        if xyz[1] == 1:
            axis_q += 'y, '
        if xyz[2] == 1:
            axis_q += 'z, '

        users_ids = self.get_updated_users_ids()

        prepared = self._session.prepare(
            'select ' + axis_q + 'timestamp from sensor_data where user_id = ? and fall_status = 0 allow filtering'
        )

        user_sd = {}
        for user_id in users_ids:
            rows = self._session.execute(prepared, (user_id,))
            if len(rows.current_rows) == 0:
                user_sd[user_id] = np.empty(0)
                continue
            user_sd[user_id] = np.ndarray((len(rows.current_rows), active_axes_count + 1), dtype=np.dtype('double'))
            user_sd[user_id][:, 0] = np.array([row.timestamp for row in rows.current_rows])
            f_axis_idx = 1
            if xyz[0] == 1:
                user_sd[user_id][:, f_axis_idx] = np.array([row.x for row in rows.current_rows])
                f_axis_idx += 1
            if xyz[1] == 1:
                user_sd[user_id][:, f_axis_idx] = np.array([row.y for row in rows.current_rows])
                f_axis_idx += 1
            if xyz[2] == 1:
                user_sd[user_id][:, f_axis_idx] = np.array([row.z for row in rows.current_rows])
                f_axis_idx += 1

        return user_sd

    def update_fall_statuses(self, statuses_dict):
        prepared = self._session.prepare(
            'update sensor_data set fall_status = ? where user_id = ? and timestamp = ?'
        )
        for user_id, statuses_tuples in statuses_dict.items():
            for status_tuple in statuses_tuples:
                self._session.execute_async(prepared, (status_tuple[1], user_id, status_tuple[0]))

    def get_updated_users_ids(self):
        rows = self._session.execute(
            'select user_id from update_info where is_updated = true'
        )
        return [row.user_id for row in rows]

    def set_not_updated_status(self, users_ids):
        prepared = self._session.prepare(
            'update update_info set is_updated = false where user_id = ?'
        )
        for user_id in users_ids:
            self._session.execute(prepared, (user_id,))

    def stop(self):
        if self._cluster is not None:
            self._cluster.shutdown()
