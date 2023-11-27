--liquibase formatted sql

--changeset jbiesek:10

alter table _comment
    rename column user_id to assignment_id;

alter
index idx_comment_user_date rename to idx_comment_assignment_date;

alter
index idx_comment_user_counter rename to idx_comment_assignment_counter;

alter table _comment
drop
constraint comment_user_id_fk;

alter table _comment
    add constraint comment_assignment_id_fk
        foreign key (assignment_id) references _assignment;

