insert into _comment (description, title, user_id, last_used_date, color, counter, visible)
values ('desc 1', 'title 1', (select id from _user where email = 'zofia_danielska@gmail.com'), now(), 1, 0, true),
       ('desc 2', 'title 2', (select id from _user where email = 'zofia_danielska@gmail.com'), now(), 1, 0, true),
       ('desc 3', 'title 3', (select id from _user where email = 'zofia_danielska@gmail.com'), now(), 1, 0, true),
       ('desc 4', 'title 4', (select id from _user where email = 'zofia_danielska@gmail.com'), now(), 1, 0, false),
       ('desc 5', 'title 5', (select id from _user where email = 'jan_kowalski@gmail.com'), now(), 1, 0, true),
       ('desc 6', 'title 6', (select id from _user where email = 'jan_kowalski@gmail.com'), now(), 1, 0, true),
       ('desc 7', 'title 7', (select id from _user where email = 'jan_kowalski@gmail.com'), now(), 1, 0, true),
       ('desc 8', 'title 8', (select id from _user where email = 'jan_kowalski@gmail.com'), now(), 1, 0, false),
       ('desc 9', 'title 9', (select id from _user where email = 'anna_malinowska@gmail.com'), now(), 1, 0, true),
       ('desc 10', 'title 10', (select id from _user where email = 'anna_malinowska@gmail.com'), now(), 1, 0, true),
       ('desc 11', 'title 11', (select id from _user where email = 'anna_malinowska@gmail.com'), now(), 1, 0, true),
       ('desc 12', 'title 12', (select id from _user where email = 'anna_malinowska@gmail.com'), now(), 1, 0, true);