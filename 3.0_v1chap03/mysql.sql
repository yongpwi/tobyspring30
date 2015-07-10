GRANT ALL PRIVILEGES ON `toby`.* TO toby@localhost
    IDENTIFIED BY 'toby2015' WITH GRANT OPTION;

create database toby;

use toby;

create table `toby`.users (
    id varchar(10) primary key,
    name varchar(20) not null,
    password varchar(10) not null
);