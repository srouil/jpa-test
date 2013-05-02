delete from department_project;
delete from employee;
delete from department;
delete from project;

/* drop-create sequence to ensure that entities receives same ID for each test */
drop sequence hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;
