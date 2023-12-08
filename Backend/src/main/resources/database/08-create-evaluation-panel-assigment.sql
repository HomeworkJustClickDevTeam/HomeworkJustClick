create table _evaluation_panel_assignment
(
    id                  serial  not null
        primary key,
    evaluation_panel_id integer not null
        constraint evaluation_panel_assignment_evaluation_panel_id_fk
            references _evaluation_panel (id),
    assignment_id       integer not null
        constraint evaluation_panel_assignment_assignment_id_fk
            references _assignment (id)
);