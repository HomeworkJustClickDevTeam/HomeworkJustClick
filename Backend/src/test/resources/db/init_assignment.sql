insert into _assignment (auto_penalty, completion_datetime, creation_datetime, last_modified_datetime, max_points,
                         task_description, title, visible, group_id, user_id)
values (50, now(), now(), now(), 10, 'test opis', 'test tytul', true, (select id from _group where name = 'grupa 1'),
        (select id from _user where email = 'zofia_danielska@gmail.com')),
       (50, now(), now(), now(), 10, 'test opis 2', 'test tytul 2', true,
        (select id from _group where name = 'grupa 1'),
        (select id from _user where email = 'zofia_danielska@gmail.com')),
       (50, now(), now(), now(), 10, 'test opis 3', 'test tytul 3', true,
        (select id from _group where name = 'grupa 2'), (select id from _user where email = 'jan_kowalski@gmail.com')),
       (50, now(), now(), now(), 10, 'test opis 4', 'test tytul 4', true,
        (select id from _group where name = 'grupa 2'), (select id from _user where email = 'jan_kowalski@gmail.com')),
       (50, now(), now(), now(), 10, 'test opis 5', 'test tytul 5', true,
        (select id from _group where name = 'grupa 3'),
        (select id from _user where email = 'anna_malinowska@gmail.com')),
       (50, now(), now(), now(), 10, 'test opis 6', 'test tytul 6', true,
        (select id from _group where name = 'grupa 3'),
        (select id from _user where email = 'anna_malinowska@gmail.com'));