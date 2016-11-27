create keyspace white_snow
with replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

create table white_snow.sensor_data(
    user_id varchar,
    x float,
    y float,
    z float,
    lat double,
    lon double,
    timestamp bigint,
    fall_status int,
    primary key(user_id, timestamp)
) with clustering order by (timestamp desc);

create table white_snow.update_info(
    user_id varchar,
    is_updated boolean,
    primary key(user_id)
);

create index if not exists user_updated_index
on white_snow.update_info(is_updated);
