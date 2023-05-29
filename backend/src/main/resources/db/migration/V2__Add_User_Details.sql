create sequence user_details_app_id_sequence start with 1 increment by 1;


create table user_details_app (
      locked boolean not null,
      id bigint not null DEFAULT nextval('user_details_app_id_sequence'),
      email varchar(255) not null,
      password varchar(255) not null,
      primary key (id),
      constraint user_details_app_email_key unique (email)
);

ALTER SEQUENCE user_details_app_id_sequence
    OWNED BY user_details_app.id;

alter table customer
    add column user_details_app_id bigint not null;

alter table customer
    add constraint user_details_app_id_fk
    foreign key (user_details_app_id)
    references user_details_app;