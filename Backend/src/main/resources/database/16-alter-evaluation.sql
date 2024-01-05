--liquibase formatted sql

--changeset jbiesek:16

alter table _evaluation
    add comment varchar(500);