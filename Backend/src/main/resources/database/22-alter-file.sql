--liquibase formatted sql

--changeset jbiesek:22


alter table _file
    alter column format type varchar(10) using format::varchar(10);

alter table _file
    alter column name type varchar(300) using name::varchar(300);