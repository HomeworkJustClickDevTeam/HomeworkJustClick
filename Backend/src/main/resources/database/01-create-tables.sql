--liquibase formatted sql

--changeset jbiesek:1
create table _user
(
    id          serial       not null
        primary key,
    color       integer      not null
        constraint _user_color_check
            check ((color <= 19) AND (color >= 0)),
    email       varchar(255) not null,
    firstname   varchar(255) not null,
    index       integer      not null,
    is_verified boolean      not null,
    lastname    varchar(255) not null,
    password    varchar(255) not null,
    role        varchar(255) not null,
    salt        varchar(255) not null
);

create unique index user_email_uix on _user (email);

create table _group
(
    id          serial       not null
        primary key,
    color       integer      not null
        constraint _group_color_check
            check ((color <= 19) AND (color >= 0)),
    description varchar(255),
    is_archived boolean      not null default false,
    name        varchar(255) not null
);

create table _group_student
(
    id          serial  not null
        primary key,
    description varchar(255),
    group_id    integer not null
        constraint fk_group
            references _group,
    user_id     integer not null
        constraint fk_student
            references _user
);

create unique index group_student_uix on _group_student (group_id, user_id);

create table _group_teacher
(
    id          serial  not null
        primary key,
    description varchar(255),
    group_id    integer not null
        constraint fk_group
            references _group,
    user_id     integer not null
        constraint fk_teacher
            references _user
);

create unique index group_teacher_uix on _group_teacher (group_id, user_id);

create table _assignment
(
    id                     serial                      not null
        primary key,
    auto_penalty           integer                     not null,
    completion_datetime    timestamp(6) with time zone,
    creation_datetime      timestamp(6) with time zone not null,
    last_modified_datetime timestamp(6) with time zone,
    max_points             integer                     not null,
    task_description       varchar(1000),
    title                  varchar(255)                not null,
    visible                boolean default true        not null,
    group_id               integer                     not null
        constraint fk_group
            references _group,
    user_id                integer                     not null
        constraint fk_user
            references _user
);

create table _solution
(
    id                     serial                      not null
        primary key,
    comment                varchar(255),
    creation_datetime      timestamp(6) with time zone not null,
    last_modified_datetime timestamp(6) with time zone,
    assignment_id          integer                     not null
        constraint fk_assignment
            references _assignment,
    group_id               integer                     not null
        constraint fk_group
            references _group,
    user_id                integer                     not null
        constraint fk_user
            references _user
);

create table _evaluation
(
    id                     serial
        primary key,
    creation_datetime      timestamp(6) with time zone not null,
    grade                  double precision            not null,
    last_modified_datetime timestamp(6) with time zone,
    result                 double precision            not null,
    group_id               integer                     not null
        constraint fk_group
            references _group,
    solution_id            integer                     not null
        constraint fk_solution
            references _solution,
    user_id                integer                     not null
        constraint fk_user
            references _user
);

create table _file
(
    id            serial       not null
        primary key,
    format        varchar(255) not null,
    mongo_id      varchar(255) not null,
    name          varchar(255) not null,
    assignment_id integer
        constraint fk_assignment
            references _assignment,
    solution_id   integer
        constraint fk_solution
            references _solution
);

create table _comment
(
    id          serial       not null
        primary key,
    description varchar(255),
    title       varchar(255) not null,
    user_id     integer      not null
        constraint fk_user
            references _user
);

create table _comment_evaluation
(
    id            serial  not null
        primary key,
    description   varchar(255),
    comment_id    integer not null
        constraint fk_comment
            references _comment,
    evaluation_id integer not null
        constraint fk_evaluation
            references _evaluation
);
