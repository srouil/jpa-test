/* drop-create sequence to set initial value other than 1. Increment size must match allocationSize of JPA annotation @SequenceGenerator */
drop sequence event_seq;
create sequence event_seq start with 1000 increment by 1;

