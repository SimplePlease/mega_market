create database mega_market;
create user postgres;
grant all privileges on database mega_market to postgres;

create table if not exists shop_unit (
    id uuid primary key,
    name text not null,
    date timestamp not null,
    parent_id uuid references shop_unit(id),
    type text not null,
    price bigint
);
