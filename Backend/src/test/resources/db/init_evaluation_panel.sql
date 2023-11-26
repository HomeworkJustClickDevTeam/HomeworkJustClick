insert into _evaluation_button(points)
values (0),
       (1),
       (2),
       (3),
       (4),
       (5);

insert into _evaluation_panel(name, width, counter, last_used_date, user_id)
values ('test 1', 3, 0, now(), (select id from _user where email = 'jan_kowalski@gmail.com')),
       ('test 2', 4, 0, now(), (select id from _user where email = 'anna_malinowska@gmail.com'));

insert into _evaluation_panel_button(evaluation_button_id, evaluation_panel_id)
values ((select id from _evaluation_button where points = 0), (select id from _evaluation_panel where name = 'test 1')),
       ((select id from _evaluation_button where points = 1), (select id from _evaluation_panel where name = 'test 1')),
       ((select id from _evaluation_button where points = 2), (select id from _evaluation_panel where name = 'test 1')),
       ((select id from _evaluation_button where points = 3), (select id from _evaluation_panel where name = 'test 1')),
       ((select id from _evaluation_button where points = 0), (select id from _evaluation_panel where name = 'test 2')),
       ((select id from _evaluation_button where points = 2), (select id from _evaluation_panel where name = 'test 2')),
       ((select id from _evaluation_button where points = 4), (select id from _evaluation_panel where name = 'test 2')),
       ((select id from _evaluation_button where points = 5), (select id from _evaluation_panel where name = 'test 2'));