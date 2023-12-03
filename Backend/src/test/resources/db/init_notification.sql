insert into _notification (description, read, date, assignment_id, user_id)
values ('dodano zadanie', false, now(), (select id from _assignment where title = 'test tytul'),
        (select id from _user where email = 'jan_kowalski@gmail.com')),
       ('dodano zadanie', false, now(), (select id from _assignment where title = 'test tytul 2'),
        (select id from _user where email = 'jan_kowalski@gmail.com')),
       ('dodano zadanie', false, now(), (select id from _assignment where title = 'test tytul 5'),
        (select id from _user where email = 'jan_kowalski@gmail.com')),
       ('oceniono zadanie', false, now(), (select id from _assignment where title = 'test tytul'),
        (select id from _user where email = 'anna_malinowska@gmail.com')),
       ('oceniono zadanie', false, now(), (select id from _assignment where title = 'test tytul 2'),
        (select id from _user where email = 'anna_malinowska@gmail.com')),
       ('zgłoszono zadanie', false, now(), (select id from _assignment where title = 'test tytul'),
        (select id from _user where email = 'zofia_danielska@gmail.com'));