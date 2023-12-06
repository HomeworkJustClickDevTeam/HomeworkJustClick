--liquibase formatted sql

--changeset jbiesek:15

alter table _evaluation
    drop column reported;

create table _evaluation_report
(
    id            serial  not null
        primary key,
    comment       varchar(255),
    evaluation_id integer not null
        constraint evaluation_report_evaluation_id_fk
            references _evaluation (id)
            on delete cascade
);

create unique index evaluation_report_evaluation_id_uix on _evaluation_report (evaluation_id);