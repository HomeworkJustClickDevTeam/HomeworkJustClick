--liquibase formatted sql

--changeset jbiesek:19

alter table _notification
    alter column description type varchar(500) using description::varchar(500);
