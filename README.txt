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
<logger category="org.hibernate.SQL">
	<level name="DEBUG" />
</logger>
<logger category="org.hibernate.type.descriptor.sql.BasicBinder">
	<level name="TRACE" />
</logger>

Add following configuration to standalone.xml to use an oracle datasource. 
This requires also Oracle module located under documentation/jboss modules. 

<datasource jndi-name="java:jboss/datasources/OracleDS" pool-name="OracleDS">
	<connection-url>jdbc:oracle:thin:@ejpdxd9007.isc.ejpd.admin.ch:25000:gendb3e</connection-url>
	<driver>com.oracle</driver>
	<transaction-isolation>TRANSACTION_READ_COMMITTED</transaction-isolation>
	<pool>
		<min-pool-size>1</min-pool-size>
		<max-pool-size>10</max-pool-size>
		<prefill>true</prefill>
	</pool>
	<security>
		<user-name>barbara</user-name>
		<password>barbara$mts</password>
	</security>
	<statement>
		<prepared-statement-cache-size>32</prepared-statement-cache-size>
		<share-prepared-statements />
	</statement>
</datasource>

<driver name="com.oracle" module="com.oracle">
	<xa-datasource-class>oracle.jdbc.driver.OracleDriver</xa-datasource-class>
</driver>


