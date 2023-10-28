insert into _comment_file_text (highlight_start, highlight_end, color, comment_id, file_id)
values (10, 20, 1, (select id from _comment where title = 'title 1'), (select id from _file where mongo_id = '1')),
       (10, 20, 2, (select id from _comment where title = 'title 3'), (select id from _file where mongo_id = '3')),
       (10, 20, 3, (select id from _comment where title = 'title 5'), (select id from _file where mongo_id = '5')),
       (10, 20, 4, (select id from _comment where title = 'title 7'), (select id from _file where mongo_id = '7')),
       (10, 20, 5, (select id from _comment where title = 'title 9'), (select id from _file where mongo_id = '9')),
       (10, 20, 6, (select id from _comment where title = 'title 11'), (select id from _file where mongo_id = '11'));