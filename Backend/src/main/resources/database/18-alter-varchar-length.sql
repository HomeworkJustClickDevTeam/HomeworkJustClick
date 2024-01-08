--liquibase formatted sql

--changeset jbiesek:17

alter table _assignment
    alter column task_description type varchar(1500) using task_description::varchar(1500);

alter table _solution
    alter column comment type varchar(1500) using comment::varchar(1500);

alter table _evaluation
    alter column comment type varchar(1500) using comment::varchar(1500);