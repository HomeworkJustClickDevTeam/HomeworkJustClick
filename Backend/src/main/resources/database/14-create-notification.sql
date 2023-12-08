--liquibase formatted sql

--changeset jbiesek:14

create table _notification
(
    id            serial                      not null
        primary key,
    description   varchar(255)                not null,
    read          boolean                     not null default false,
    date          timestamp(6) with time zone not null default now(),
    assignment_id integer                     not null
        constraint notification_assignment_id_fk
            references _assignment (id)
            on delete cascade,
    user_id       integer                     not null
        constraint notification_user_id_fk
            references _user (id)
            on delete cascade
);

create index idx_notification_user on _notification (user_id);

create index idx_notification_read on _notification (read);