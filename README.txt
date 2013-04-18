jpa-test
========

A collection of unit tests about JPA and Hibernate

Run all tests with following command:

mvn clean test

Profile arq-jbossas-managed-7 for testing in managed JBoss AS 7 is active by default.

Build will download and unpack a JBoss AS 7 release under target. 

Following must be done in standalone.xml to enable SQL parameter logging with managed JBoss AS 7:

<console-handler name="CONSOLE">
	<level name="TRACE" />
	...
</console-handler>
...
<logger category="org.hibernate.type.descriptor.sql.BasicBinder">
	<level name="TRACE" />
</logger>
