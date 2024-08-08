create table if not exists `users`
(
    id         bigint auto_increment primary key,
    name       varchar(50) charset utf8mb4 not null,
    created_at timestamp                   not null default current_timestamp,
    updated_at timestamp                   not null default current_timestamp on update current_timestamp,
    constraint users_name_uq unique (name)
);

create table if not exists `rooms`
(
    id         bigint auto_increment primary key,
    name       varchar(50) charset utf8mb4 not null,
    type       enum ('Friend', 'Group')    not null,
    created_at timestamp                   not null default current_timestamp,
    updated_at timestamp                   not null default current_timestamp on update current_timestamp,
    constraint chat_rooms_name_uq unique (name)
);

create table if not exists `room_users`
(
    id         bigint auto_increment primary key,
    user_id    bigint    not null,
    room_id    bigint    not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    constraint room_users_user_id_fk foreign key (user_id) references users (id),
    constraint room_users_room_id_fk foreign key (room_id) references rooms (id),
    constraint room_users_user_id_room_id_uq unique (user_id, room_id)
);

create table if not exists `messages`
(
    id         bigint auto_increment primary key,
    sender_id  bigint                       not null,
    room_id    bigint                       not null,
    message    varchar(255) charset utf8mb4 not null,
    created_at timestamp                    not null default current_timestamp,
    updated_at timestamp                    not null default current_timestamp on update current_timestamp,
    constraint messages_sender_id_fk foreign key (sender_id) references users (id),
    constraint messages_room_id_fk foreign key (room_id) references rooms (id)
);