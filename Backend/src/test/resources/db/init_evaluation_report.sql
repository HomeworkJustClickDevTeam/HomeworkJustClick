insert into _evaluation_report (comment, evaluation_id)
values ('comment 1', (select id from _evaluation where result = 2)),
       ('comment 1', (select id from _evaluation where result = 4)),
       ('comment 1', (select id from _evaluation where result = 6)),
       ('comment 1', (select id from _evaluation where result = 8)),
       ('comment 1', (select id from _evaluation where result = 10));