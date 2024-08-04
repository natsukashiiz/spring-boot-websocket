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
    created_at timestamp                   not null default current_timestamp,
    updated_at timestamp                   not null default current_timestamp on update current_timestamp,
    constraint chat_rooms_name_uq unique (name)
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