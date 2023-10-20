insert into _group_student (description, group_id, user_id)
values ('', (select id from _group where name = 'grupa 1'),
        (select id from _user where email = 'jan_kowalski@gmail.com')),
       ('', (select id from _group where name = 'grupa 1'),
        (select id from _user where email = 'anna_malinowska@gmail.com')),
       ('', (select id from _group where name = 'grupa 2'),
        (select id from _user where email = 'anna_malinowska@gmail.com')),
       ('', (select id from _group where name = 'grupa 2'),
        (select id from _user where email = 'zofia_danielska@gmail.com')),
       ('', (select id from _group where name = 'grupa 3'),
        (select id from _user where email = 'zofia_danielska@gmail.com')),
       ('', (select id from _group where name = 'grupa 3'),
        (select id from _user where email = 'jan_kowalski@gmail.com'));