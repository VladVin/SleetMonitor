from cassandra.cluster import Cluster
import numpy as np
from storage.DataInterpolator import DataInterpolator

class DBManager(object):
    def __init__(self):
        self._keyspace_host = '127.0.0.1'
        self._keyspace = 'white_snow'
        self._cluster = Cluster([self._keyspace_host])
        self._session = self._cluster.connect(self._keyspace)

    def get_fresh_sensor_data(self):
        users_ids = self.get_updated_users_ids()
        self.set_not_updated_status(users_ids)

        prepared = self._session.prepare(
            'select x, y, z, timestamp from sensor_data where user_id = ? and fall_status = -1 allow filtering'
        )

        user_sd = {}
        for user_id in users_ids:
            rows = self._session.execute(prepared, (user_id,))
            if len(rows.current_rows) == 0:
                continue
            user_sd[user_id] = np.ndarray((len(rows.current_rows), 4), dtype=np.dtype('double'))
            user_sd[user_id][:, :] = np.array([[row.timestamp, row.x, row.y, row.z] for row in rows])

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
