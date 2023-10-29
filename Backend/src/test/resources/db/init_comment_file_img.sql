insert into _comment_file_img (left_top_x, left_top_y, width, height, line_width, color, comment_id, file_id, img_width,
                               img_height)
values (100, 100, 100, 100, 2, 2, (select id from _comment where title = 'title 2'),
        (select id from _file where mongo_id = '2'), 1920, 1080),
       (200, 200, 200, 200, 4, 4, (select id from _comment where title = 'title 4'),
        (select id from _file where mongo_id = '4'), 1920, 1080),
       (300, 300, 300, 300, 6, 6, (select id from _comment where title = 'title 6'),
        (select id from _file where mongo_id = '6'), 1920, 1080),
       (400, 400, 400, 400, 8, 8, (select id from _comment where title = 'title 8'),
        (select id from _file where mongo_id = '8'), 1920, 1080),
       (500, 500, 500, 500, 10, 10, (select id from _comment where title = 'title 10'),
        (select id from _file where mongo_id = '10'), 1920, 1080),
       (600, 600, 600, 600, 12, 12, (select id from _comment where title = 'title 12'),
        (select id from _file where mongo_id = '12'), 1920, 1080);