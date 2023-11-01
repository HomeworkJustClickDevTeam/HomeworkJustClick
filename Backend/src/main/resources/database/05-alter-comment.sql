alter table _comment
alter
column default_color type varchar(10) using default_color::varchar(10);

alter table _comment_file_img
alter
column color type varchar(10) using color::varchar(10);

alter table _comment_file_text
alter
column color type varchar(10) using color::varchar(10);