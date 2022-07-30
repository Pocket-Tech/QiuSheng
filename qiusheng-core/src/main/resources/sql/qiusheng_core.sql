create table if not exists song
(
    sid int not null,
    length int not null,
    bpm int not null,
    title varchar(50) not null,
    artist varchar(50) not null,
    s_mode int not null,
    time int not null,
    s_md5 varchar(50) not null,
    s_file_path varchar(200) not null,
    img_md5 varchar(50) not null,
    img_file_path varchar(100) not null,
    constraint song_pk
        primary key (sid)
);
create table if not exists user
(
    uid int auto_increment,
    user_name varchar(50) not null,
    constraint user_pk
        primary key (uid)
);
create table if not exists chart
(
    cid int not null,
    sid int not null,
    uid int null,
    version varchar(50) not null,
    level int not null,
    type int not null,
    size long not null,
    c_mode int not null,
    c_md5 varchar(50) not null,
    c_file_path varchar(200) not null,
    constraint chart_pk
        primary key (cid),
    constraint chart_song_sid_fk
        foreign key (sid) references song (sid),
    constraint chart_user_uid_fk
        foreign key (uid) references `user` (uid)
);
create table if not exists events
(
    eid      int auto_increment,
    `name`     varchar(50)  not null,
    sponsor  varchar(50)  not null,
    `start`    date         not null,
    `end`      date         not null,
    cover    varchar(200) null,
    active   int          not null,
    cid_list varchar(400) not null,
    constraint table_name_pk
        primary key (eid)
);
create table if not exists admin
(
    id       int auto_increment,
    username varchar(30)           not null,
    password varchar(30)           not null,
    enable   boolean default true  not null,
    locked   boolean default false not null,
    constraint admin_pk
        primary key (id)
);
