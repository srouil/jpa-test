jpa-test
========

A collection of unit tests about JPA and Hibernate

Run all tests with following command:

mvn clean test

Profile arq-jbossas-managed-7 for testing in managed JBoss AS 7 is active by default.
Build will download and unpack a JBoss AS 7 release under target. 

Following must be done to debug in unit test code with Eclipse because unit tests use managed container:
- modify arquillian.xml to uncomment debug parameters
- Start a unit test with Eclipse Run As. JBoss AS will listen on debug port and suspend its execution
- Create a "Remote Java Application" debug configuration connecting on port 8787 and start it in debug mode

Following must be done in standalone.xml to enable SQL parameter logging with managed JBoss AS 7:

<console-handler name="CONSOLE">
	<level name="TRACE" />
	...
</console-handler>
...
<logger category="org.hibernate.type.descriptor.sql.BasicBinder">
	<level name="TRACE" />
</logger>
