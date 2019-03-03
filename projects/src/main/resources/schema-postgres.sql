create table evaluee (id bigint generated by default as identity, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, id_user bigint, project_id bigint, primary key (id));
create table evaluee_feedback_provider (id bigint generated by default as identity, created_on timestamp, relationship varchar(255), status varchar(255), evaluee_id bigint not null, feedback_provider_id bigint not null, primary key (id));
create table evaluee_reviewer (id bigint generated by default as identity, evaluee_id bigint not null, reviewer_id bigint not null, primary key (id));
create table feedback_provider (id bigint generated by default as identity, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, id_user bigint, project_id bigint, primary key (id));
create table project (id bigint generated by default as identity, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, description varchar(255), id_evaluation_template bigint, id_report_template bigint, name varchar(255), status varchar(255), primary key (id));
create table project_admin (id bigint generated by default as identity, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, id_user bigint, creator boolean, project_id bigint, primary key (id));
create table reviewer (id bigint generated by default as identity, created_by varchar(255), created_date timestamp not null, modified_by varchar(255), modified_date timestamp, id_user bigint, project_id bigint, primary key (id));
alter table project add constraint UK_3k75vvu7mevyvvb5may5lj8k7 unique (name);
alter table evaluee add constraint FKiebdu1132vxbllmwpgv2mg2m3 foreign key (project_id) references project;
alter table evaluee_feedback_provider add constraint FKn888ucy87vgs4nrdaui55vxls foreign key (evaluee_id) references evaluee on delete cascade;
alter table evaluee_feedback_provider add constraint FKntgsgupt6tiq9me29tigabtnq foreign key (feedback_provider_id) references feedback_provider on delete cascade;
alter table evaluee_reviewer add constraint FK3gj1pyb6nd8dxiw0rtu7hotqd foreign key (evaluee_id) references evaluee on delete cascade;
alter table evaluee_reviewer add constraint FKaprtt3nk785qgsu38jbgy5u96 foreign key (reviewer_id) references reviewer on delete cascade;
alter table feedback_provider add constraint FK9ijj6u8wxyitemh20ctnjpv2r foreign key (project_id) references project;
alter table project_admin add constraint FKhwqdy6kbc6g50o6dqig2g2hrd foreign key (project_id) references project;
alter table reviewer add constraint FK3st5tmjuru1q61xygqnqr40h1 foreign key (project_id) references project;
