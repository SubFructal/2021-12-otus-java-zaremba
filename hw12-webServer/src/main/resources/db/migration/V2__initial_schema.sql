drop table client;

create table address
(
    id bigint not null primary key,
    street varchar(255)
);

create table client
(
    id bigint not null primary key,
    name varchar(255),
    address_id bigint
);

create table phone
(
    id bigint not null primary key,
    phone_number varchar(255),
    client_id bigint
);
