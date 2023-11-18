alter table _comment
    add column visible boolean not null default true;

alter table _comment_file_img
drop
column line_width;