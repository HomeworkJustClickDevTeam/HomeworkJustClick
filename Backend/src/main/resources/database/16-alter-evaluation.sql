--liquibase formatted sql

--changeset jbiesek:15

alter table _evaluation
    add comment varchar(500);