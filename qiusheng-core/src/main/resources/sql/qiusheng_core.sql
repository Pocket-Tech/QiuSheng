create database if not exists qiusheng_core CHARACTER SET utf8 COLLATE utf8_general_ci;
use qiusheng_core;
create table if not exists song
(
    sid int not null,
    length int null,
    bpm int null,
    title varchar(50) null,
    artist varchar(50) null,
    s_mode int null,
    time int null,
    s_md5 varchar(50) null,
    s_file_path varchar(200) null,
    img_md5 varchar(50) null,
    img_file_path varchar(100) null,
    constraint song_pk
        primary key (sid)
);
create table if not exists user
(
    uid int auto_increment,
    user_name varchar(50) null,
    constraint user_pk
        primary key (uid)
);
create table if not exists chart
(
    cid int not null,
    sid int null,
    uid int null,
    version varchar(50) null,
    level int null,
    type int null,
    size long null,
    c_mode int null,
    c_md5 varchar(50) null,
    c_file_path varchar(200) null,
    constraint chart_pk
        primary key (cid),
    constraint chart_song_sid_fk
        foreign key (sid) references song (sid),
    constraint chart_user_uid_fk
        foreign key (uid) references user (uid)
);
create table if not exists events
(
    eid int auto_increment,
    sid int null,
    constraint events_pk
        primary key (eid),
    constraint events_song_sid_fk
        foreign key (sid) references song (sid)
);





