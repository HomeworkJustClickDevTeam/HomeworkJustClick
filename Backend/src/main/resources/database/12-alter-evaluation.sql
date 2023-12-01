--liquibase formatted sql

--changeset jbiesek:12

alter table _evaluation
    add reported boolean default false not null;
