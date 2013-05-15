create table PERSON_EMAIL_ADDRESS (PERSON_ID bigint not null, ADDRESS varchar(255));
create table PERSON_PHONE_NUMBER (PERSON_ID bigint not null, number varchar(255), type varchar(255));
create table PERSON (ID bigint not null, FIRST_NAME varchar(255), LAST_NAME varchar(255), primary key (id));
alter table PERSON_EMAIL_ADDRESS add constraint FK_PERS_EMAIL_ADDR_PERS foreign key (PERSON_ID) references PERSON;
alter table PERSON_PHONE_NUMBER add constraint FK_PERS_PHONE_PERS foreign key (PERSON_ID) references PERSON;