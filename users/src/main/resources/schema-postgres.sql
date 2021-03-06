create table authority (id SERIAL NOT NULL, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, name varchar(50) not null, primary key (id));
create table "user" (id SERIAL NOT NULL, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, enabled boolean not null, lastpasswordresetdate timestamp, mail varchar(100) not null, password varchar(100) not null, username varchar(50) not null, primary key (id));
create table user_authority (user_id bigint not null, authority_id bigint not null);
alter table "user" add constraint UK_sb8bbouer5wak8vyiiy4pf2bx unique (username);
alter table user_authority add constraint FKgvxjs381k6f48d5d2yi11uh89 foreign key (authority_id) references authority;
alter table user_authority add constraint FKpqlsjpkybgos9w2svcri7j8xy foreign key (user_id) references "user";