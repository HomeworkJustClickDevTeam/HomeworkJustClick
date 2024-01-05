--liquibase formatted sql

--changeset jbiesek:17

alter table _assignment
    add advanced_evaluation boolean;