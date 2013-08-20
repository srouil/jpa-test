/* drop-create sequence to set initial value other than 1. Increment size must match allocationSize of JPA annotation @SequenceGenerator */
drop sequence sys_event_seq;
create sequence sys_event_seq start with 1000 increment by 1;
drop sequence alert_event_seq;
create sequence alert_event_seq start with 10 increment by 1;

