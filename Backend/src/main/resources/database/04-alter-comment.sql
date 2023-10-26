alter table _comment
    add column counter integer not null default 0;

alter table _comment_file_img
    add column img_width integer not null default 0;

alter table _comment_file_img
    add column img_height integer not null default 0;