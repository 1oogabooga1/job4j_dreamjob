create table candidates (
id serial primary key,
name text,
description text,
creationDate timestamp,
city_id int references cities(id),
file_id int references files(id)
);