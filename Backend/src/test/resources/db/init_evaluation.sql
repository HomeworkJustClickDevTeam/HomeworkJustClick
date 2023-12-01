insert into _evaluation (creation_datetime, grade, last_modified_datetime, result, group_id, solution_id, user_id,
                         reported)
values (now(), 3, now(), 1, (select id from _group where name = 'grupa 1'),
        (select id from _solution where comment = 'test solution'),
        (select id from _user where email = 'zofia_danielska@gmail.com'), false),
       (now(), 3, now(), 2, (select id from _group where name = 'grupa 1'),
        (select id from _solution where comment = 'test solution 2'),
        (select id from _user where email = 'zofia_danielska@gmail.com'), true),
       (now(), 3, now(), 3, (select id from _group where name = 'grupa 1'),
        (select id from _solution where comment = 'test solution 3'),
        (select id from _user where email = 'zofia_danielska@gmail.com'), false),
       (now(), 3, now(), 4, (select id from _group where name = 'grupa 1'),
        (select id from _solution where comment = 'test solution 4'),
        (select id from _user where email = 'zofia_danielska@gmail.com'), true),
       (now(), 3, now(), 5, (select id from _group where name = 'grupa 2'),
        (select id from _solution where comment = 'test solution 5'),
        (select id from _user where email = 'jan_kowalski@gmail.com'), false),
       (now(), 3, now(), 6, (select id from _group where name = 'grupa 2'),
        (select id from _solution where comment = 'test solution 6'),
        (select id from _user where email = 'jan_kowalski@gmail.com'), true),
       (now(), 3, now(), 7, (select id from _group where name = 'grupa 2'),
        (select id from _solution where comment = 'test solution 7'),
        (select id from _user where email = 'jan_kowalski@gmail.com'), false),
       (now(), 3, now(), 8, (select id from _group where name = 'grupa 2'),
        (select id from _solution where comment = 'test solution 8'),
        (select id from _user where email = 'jan_kowalski@gmail.com'), true),
       (now(), 3, now(), 9, (select id from _group where name = 'grupa 3'),
        (select id from _solution where comment = 'test solution 9'),
        (select id from _user where email = 'anna_malinowska@gmail.com'), false),
       (now(), 3, now(), 10, (select id from _group where name = 'grupa 3'),
        (select id from _solution where comment = 'test solution 10'),
        (select id from _user where email = 'anna_malinowska@gmail.com'), true),
       (now(), 3, now(), 11, (select id from _group where name = 'grupa 3'),
        (select id from _solution where comment = 'test solution 11'),
        (select id from _user where email = 'anna_malinowska@gmail.com'), false),
       (now(), 3, now(), 12, (select id from _group where name = 'grupa 3'),
        (select id from _solution where comment = 'test solution 12'),
        (select id from _user where email = 'anna_malinowska@gmail.com'), true);