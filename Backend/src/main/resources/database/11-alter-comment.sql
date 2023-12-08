--liquibase formatted sql

--changeset jbiesek:11

alter table _comment
    add column user_id integer not null;

alter table _comment
    add constraint comment_user_id_fk
        foreign key (user_id) references _user;

