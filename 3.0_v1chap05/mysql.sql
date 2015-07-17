GRANT ALL PRIVILEGES ON `toby`.* TO toby@localhost
    IDENTIFIED BY 'toby2015' WITH GRANT OPTION;

create database toby;

use toby;

create table `toby`.users (
    id varchar(10) primary key,
    name varchar(20) not null,
    password varchar(10) not null
);

alter table 'toby'.users add(level TINYINT NOT NULL );
alter table 'toby'.users add(login INT NOT NULL );
alter table 'toby'.users add(recommend INT NOT NULL );