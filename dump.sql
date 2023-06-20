create table "group"
(
    groupid     serial
        primary key,
    name        varchar(25)       not null
        constraint group_pk
            unique,
    prefix      varchar(20)       not null,
    description varchar(100)      not null,
    weight      integer default 1 not null
);

alter table "group"
    owner to "user";

create table language
(
    languageid serial
        constraint language_pk
            primary key,
    name       varchar(40) not null,
    code       varchar(2)  not null
);

alter table language
    owner to "user";

create table player
(
    uuid       varchar(36) not null
        constraint user_pkey
            primary key,
    name       varchar(255),
    languageid serial
        constraint language_languageid_fk
            references language
);

alter table player
    owner to "user";

create table joingroup
(
    uuid      varchar(36) not null
        references player,
    groupid   serial
        references "group",
    joinuntil timestamp,
    constraint joingroup_key
        unique (uuid, groupid)
);

alter table joingroup
    owner to "user";

create table sign
(
    signid serial
        primary key,
    world  varchar(100) not null,
    posx   integer      not null,
    posy   integer      not null,
    posz   integer      not null
);

alter table sign
    owner to "user";

insert into language(name,code) VALUES ('English','en');
insert into language(name,code) VALUES ('French','fr');
insert into language(name,code) VALUES ('German','de');

insert into "group"(name,prefix,description,weight) VALUES('player','&ePlayer','description',10);