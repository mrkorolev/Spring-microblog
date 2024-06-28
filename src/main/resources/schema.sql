DROP TABLE IF EXISTS posts_tags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
    id bigserial PRIMARY KEY,
    username varchar(50) UNIQUE NOT NULL,
    password varchar(100) NOT NULL,
    is_active boolean NOT NULL,
    dt_created timestamp NOT NULL,
    dt_updated timestamp
);

INSERT INTO users(username, password, is_active, dt_created)
VALUES ('nikkorolev.25', '$2a$10$8zQqkwf4WaAwM3mS7oZl7OMc0jOxhHlSG1P8zcW3zisSrUjFNARpu', true, '2024-06-20 03:39:30'::timestamp),
       ('matt.walters', '$2a$10$qM9GKvMWVwfMQ5ELUSAroeUfedWBn2G8EwL52SkOlshrGkdYlH0xy', true, '2024-06-21 03:39:30'::timestamp),
       ('matt.savok', '$2a$10$HUsqUe5lv0oWTlOwunlbmOCXkqRrBpaofF8Yr89Y82/hZ9YcDXYVO', true, '2024-06-22 03:39:30'::timestamp),
       ('abdul.faiz', '$2a$10$IYzdJKtzcvxl1NvrhHWc8.my2Nc/k0Xyep505Mwb1dQXmv4YysUFW', true, '2024-06-23 03:39:30'::timestamp),
       ('siddig.abdul', '$2a$10$FolnEwzK5.YPFrWjnU65MOgS.8XAOgp3uLhgf2KYUgULWa/i27fsy',true, '2024-06-24 03:39:30'::timestamp),
       ('ethan.winters', '$2a$10$I0RyDauzc/Dy44zBxAj8AuIdvJtyXhfjoz.EgbKT53v8FSgAkJQiG', true, '2024-06-25 03:39:30'::timestamp),
       ('jonathan.byers', '$2a$10$tRyWt5dbB/PORhFgt5F8K.V6i6MgpGe/6c9lFP4eZwjMmz0IN2qzi', false, '2024-06-26 03:39:30'::timestamp),
       ('jessica.alba', '$2a$10$gPIyp5wYxOwpExbNFPfsG.A2RB7QapvGg17VXomg8wdnH1U/ZhlOK', true, '2024-06-27 03:39:30'::timestamp);

CREATE TABLE posts (
    id bigserial PRIMARY KEY,
    user_id bigint REFERENCES users(id),
    title varchar(100) NOT NULL,
    content text NOT NULL,
    dt_created timestamp NOT NULL,
    dt_updated timestamp
);

CREATE TABLE tags (
    id bigserial PRIMARY KEY,
    name varchar(50) UNIQUE NOT NULL
);

CREATE TABLE posts_tags (
    post_id bigint REFERENCES posts(id) ON DELETE CASCADE,
    tag_id bigint REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE comments (
    id bigserial PRIMARY KEY,
    post_id bigint REFERENCES posts(id) ON DELETE CASCADE,
    user_id bigint REFERENCES users(id),
    content text NOT NULL,
    dt_created timestamp NOT NULL,
    dt_updated timestamp
);

CREATE TABLE roles (
    id bigserial PRIMARY KEY,
    name varchar(20) UNIQUE NOT NULL
);

CREATE TABLE users_roles (
    user_id bigint REFERENCES users(id) ON DELETE CASCADE,
    role_id bigint REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO posts(title, user_id, content, dt_created, dt_updated)
VALUES
    ('Day 1', 2, 'Everything is going great!', '2024-06-19 10:16:07'::timestamp, NULL),
    ('Day 2', 2, 'Some things are not working out!', '2024-06-20 10:16:07'::timestamp, NULL),
    ('Day 3', 2, 'Everything is great again!', '2024-06-21 10:16:07'::timestamp, NULL);

INSERT INTO tags(name)
VALUES ('news'),
       ('life'),
       ('day'),
       ('mood'),
       ('ideas'),
       ('thoughts');

INSERT INTO posts_tags
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 2),
       (2, 1),
       (2, 5),
       (3, 3),
       (3, 2),
       (3, 6);

INSERT INTO comments(post_id, user_id, content, dt_created)
VALUES  (2, 2, 'Nice', '2024-06-27 21:46:26'::timestamp),
        (1, 2, 'Awesome', '2024-06-27 21:46:26'::timestamp),
        (1, 2, 'Excellent', '2024-06-27 21:46:26'::timestamp),
        (1, 3, 'Wonderful', '2024-06-27 21:46:26'::timestamp),
        (3, 3, 'Disgusting', '2024-06-27 21:46:26'::timestamp),
        (3,  2, 'Atrocious', '2024-06-27 21:46:26'::timestamp);

INSERT INTO roles(name)
VALUES ('ADMIN'), ('MODERATOR'), ('REGULAR');


INSERT INTO users_roles
VALUES (1, 1), (1, 2), (1, 3),
       (2, 2), (2, 3),
       (3, 2), (3, 3),
       (4, 2), (4, 3),
       (5, 3),
       (6, 3),
       (7, 3),
       (8, 3);
