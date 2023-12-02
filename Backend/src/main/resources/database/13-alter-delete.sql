--liquibase formatted sql

--changeset jbiesek:13

alter table _comment
drop
constraint comment_assignment_id_fk;

alter table _comment
    add constraint comment_assignment_id_fk
        foreign key (assignment_id) references _assignment
            on delete cascade;

alter table _comment_file_img
drop
constraint comment_file_img_comment_id_fk;

alter table _comment_file_img
    add constraint comment_file_img_comment_id_fk
        foreign key (comment_id) references _comment
            on delete cascade;

alter table _comment_file_img
drop
constraint comment_file_img_file_id_fk;

alter table _comment_file_img
    add constraint comment_file_img_file_id_fk
        foreign key (file_id) references _file
            on delete cascade;

alter table _comment_file_text
drop
constraint comment_file_text_comment_id_fk;

alter table _comment_file_text
    add constraint comment_file_text_comment_id_fk
        foreign key (comment_id) references _comment
            on delete cascade;

alter table _comment_file_text
drop
constraint comment_file_text_file_id_fk;

alter table _comment_file_text
    add constraint comment_file_text_file_id_fk
        foreign key (file_id) references _file
            on delete cascade;

alter table _file
drop
constraint file_assignment_id_fk;

alter table _file
    add constraint file_assignment_id_fk
        foreign key (assignment_id) references _assignment
            on delete cascade;

alter table _file
drop
constraint file_solution_id_fk;

alter table _file
    add constraint file_solution_id_fk
        foreign key (solution_id) references _solution
            on delete cascade;