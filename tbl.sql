CREATE DATABASE db;
CREATE TABLE db.tbl (
    id integer auto_increment not null,
    created datetime not null,
    primary key (id)
);
insert into db.tbl (created) values ('2015-01-01 00:00:00');
