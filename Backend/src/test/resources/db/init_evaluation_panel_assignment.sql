insert into _evaluation_panel_assignment (evaluation_panel_id, assignment_id)
values ((select id from _evaluation_panel where name = 'test 1'),
        (select id from _assignment where title = 'test tytul 3')),
       ((select id from _evaluation_panel where name = 'test 1'),
        (select id from _assignment where title = 'test tytul 4')),
       ((select id from _evaluation_panel where name = 'test 2'),
        (select id from _assignment where title = 'test tytul 5')),
       ((select id from _evaluation_panel where name = 'test 2'),
        (select id from _assignment where title = 'test tytul 6'));