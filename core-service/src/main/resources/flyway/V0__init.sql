create table public.user
(
    id       bigserial    not null primary key,
    created  timestamp,
    updated  timestamp,
    status   varchar(50)  not null,
    username varchar(250) not null unique,
    avatar   varchar(250)
);