--liquibase formatted sql

--changeset jbiesek:21


alter table _group
    alter column description type varchar(65) using description::varchar(65);

alter table _group
    alter column name type varchar(65) using name::varchar(65);