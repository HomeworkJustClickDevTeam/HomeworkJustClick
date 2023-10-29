create table _comment_file_text
(
    id              serial  not null
        primary key,
    highlight_start integer not null,
    highlight_end   integer not null,
    color           integer not null,
    comment_id      integer not null
        constraint comment_file_text_comment_id_fk
            references _comment (id),
    file_id         integer not null
        constraint comment_file_text_file_id_fk
            references _file (id)
);

create table _comment_file_img
(
    id         serial  not null
        primary key,
    left_top_x integer not null,
    left_top_y integer not null,
    width      integer not null,
    height     integer not null,
    line_width integer not null,
    color      integer not null,
    comment_id integer not null
        constraint comment_file_img_comment_id_fk
            references _comment (id),
    file_id    integer not null
        constraint comment_file_img_file_id_fk
            references _file (id)
);
