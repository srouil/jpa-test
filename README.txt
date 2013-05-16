jpa-test
========

A collection of unit tests about JPA and Hibernate

Run all tests with following command:

mvn clean test

Profile arq-jbossas-managed-7 for testing in managed JBoss AS 7 is active by default.

Build will perform following steps:
- download JBoss AS 7 release and unpack it under target.
- start JBoss AS
- run each unit test. This includes loading initial dataset, running the test itself, eventually compare test outcome with 
  expected dataset, cleanup database
- stop JBoss AS

Following must be done to debug in unit test code with Eclipse because unit tests use managed container:
- modify arquillian.xml to uncomment debug parameters
- Start a unit test with Eclipse Run As. JBoss AS will listen on debug port and suspend its execution
- Create a "Remote Java Application" debug configuration connecting on port 8787 and start it in debug mode

Following must be configured in standalone.xml to enable SQL parameter logging with managed JBoss AS 7. 
standalone.xml is overwritten by Maven build every time JBoss AS is unpacked.   

<console-handler name="CONSOLE">
	<level name="TRACE" />
	...
</console-handler>
...
<logger category="org.hibernate.type.descriptor.sql.BasicBinder">
	<level name="TRACE" />
</logger>
