SET REFERENTIAL_INTEGRITY FALSE;
delete from employee;
delete from department;
SET REFERENTIAL_INTEGRITY TRUE;

/* drop-create sequence to ensure that entities receives same ID for each test */
drop sequence hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;

