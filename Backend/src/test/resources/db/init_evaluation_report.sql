insert into _evaluation_report (comment, evaluation_id)
values ('comment 1', (select id from _evaluation where result = 2)),
       ('comment 2', (select id from _evaluation where result = 4)),
       ('comment 3', (select id from _evaluation where result = 6)),
       ('comment 4', (select id from _evaluation where result = 8)),
       ('comment 5', (select id from _evaluation where result = 10));