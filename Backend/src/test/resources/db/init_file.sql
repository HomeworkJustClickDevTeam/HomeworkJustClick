insert into _file (format, mongo_id, name, assignment_id, solution_id)
values ('txt', '1', 'test file', null, (select id from _solution where comment = 'test solution')),
       ('png', '2', 'test file 2', null, (select id from _solution where comment = 'test solution 2')),
       ('txt', '3', 'test file 3', null, (select id from _solution where comment = 'test solution 3')),
       ('png', '4', 'test file 4', null, (select id from _solution where comment = 'test solution 4')),
       ('txt', '5', 'test file 5', null, (select id from _solution where comment = 'test solution 5')),
       ('png', '6', 'test file 6', null, (select id from _solution where comment = 'test solution 6')),
       ('txt', '7', 'test file 7', null, (select id from _solution where comment = 'test solution 7')),
       ('png', '8', 'test file 8', null, (select id from _solution where comment = 'test solution 8')),
       ('txt', '9', 'test file 9', null, (select id from _solution where comment = 'test solution 9')),
       ('png', '10', 'test file 10', null, (select id from _solution where comment = 'test solution 10')),
       ('txt', '11', 'test file 11', null, (select id from _solution where comment = 'test solution 11')),
       ('png', '12', 'test file 12', null, (select id from _solution where comment = 'test solution 12'));