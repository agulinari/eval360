create table evaluation_template (id SERIAL NOT NULL, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, id_user varchar(255), title varchar(255), primary key (id));
create table item_template (id SERIAL NOT NULL, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, description varchar(255), item_type varchar(255), position integer, title varchar(255), section_id bigint, primary key (id));
create table "section" (id SERIAL NOT NULL, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, description varchar(255), name varchar(255), position integer, section_type varchar(255), template_id bigint, primary key (id));
alter table evaluation_template add constraint UK_3sbnaqwfxbrglov6egfdvv59d unique (title);
alter table item_template add constraint FK3y4bxofjt46s66v2scvpw9o5f foreign key (section_id) references "section";
alter table "section" add constraint FKau4vn2budwsygpi0r051c9j1o foreign key (template_id) references evaluation_template;
