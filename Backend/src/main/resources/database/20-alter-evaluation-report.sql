alter table _evaluation_report
    alter column comment type varchar(1500) using comment::varchar(1500);

