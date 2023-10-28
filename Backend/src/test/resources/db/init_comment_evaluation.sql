insert into _comment_evaluation (description, comment_id, evaluation_id)
values (null, (select id from _comment where title = 'title 1'), (select id from _evaluation where grade = 1)),
       (null, (select id from _comment where title = 'title 3'), (select id from _evaluation where grade = 3)),
       (null, (select id from _comment where title = 'title 5'), (select id from _evaluation where grade = 5)),
       (null, (select id from _comment where title = 'title 7'), (select id from _evaluation where grade = 7)),
       (null, (select id from _comment where title = 'title 9'), (select id from _evaluation where grade = 9)),
       (null, (select id from _comment where title = 'title 11'), (select id from _evaluation where grade = 11));