alter table _comment
    add last_used_date timestamp(6) with time zone default now() not null;

alter table _comment
    add default_color integer not null;

create index idx_comment_user_date on _comment (user_id, last_used_date);