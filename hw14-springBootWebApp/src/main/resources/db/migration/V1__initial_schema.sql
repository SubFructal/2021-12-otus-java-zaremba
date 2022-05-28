create table client
(
    id bigserial not null primary key,
    name varchar(255) not null
);

create table address
(
    id bigserial not null primary key,
    client_id bigserial not null references client (id) on delete cascade on update cascade,
    street varchar(255) not null
);

create table phone
(
    id bigserial not null primary key,
    client_id bigserial not null references client (id) on delete cascade on update cascade,
    phone_number varchar(255) not null
);
