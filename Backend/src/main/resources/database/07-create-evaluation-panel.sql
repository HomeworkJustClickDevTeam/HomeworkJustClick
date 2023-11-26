create table _evaluation_panel
(
    id             serial       not null
        primary key,
    name           varchar(255) not null,
    width          integer      not null,
    counter        integer      not null       default 0,
    last_used_date timestamp(6) with time zone default now() not null,
    user_id        integer      not null
        constraint evaluation_panel_user_id_fk
            references _user (id)
);

create table _evaluation_button
(
    id     serial           not null
        primary key,
    points double precision not null
);

create table _evaluation_panel_button
(
    id                   serial  not null
        primary key,
    evaluation_panel_id  integer not null
        constraint evaluation_panel_button_evaluation_panel_id_fk
            references _evaluation_panel (id),
    evaluation_button_id integer not null
        constraint evaluation_panel_button_evaluation_button_id_fk
            references _evaluation_button (id)
);