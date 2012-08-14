echo "args:" $1 $2 $3 $4 $5

JAVA_HOME=/usr/lib/jvm/jdk1.6.0_23/
M2_HOME=/home/carlo/.m2/repository/

${JAVA_HOME}bin/java -Dfile.encoding=UTF-8 \
-classpath "./target/classes:\
${M2_HOME}javax/jms/jms-api/1.1-rev-1/jms-api-1.1-rev-1.jar:\
${M2_HOME}org/apache/activemq/activeio-core/3.1.0/activeio-core-3.1.0.jar:\
${M2_HOME}org/apache/activemq/activemq-camel/5.2.0/activemq-camel-5.2.0.jar:\
${M2_HOME}org/apache/activemq/activemq-core/5.2.0/activemq-core-5.2.0.jar:\
${M2_HOME}org/apache/activemq/activemq-pool/5.2.0/activemq-pool-5.2.0.jar:\
${M2_HOME}aopalliance/aopalliance/1.0/aopalliance-1.0.jar:\
${M2_HOME}backport-util-concurrent/backport-util-concurrent/2.1/backport-util-concurrent-2.1.jar:\
${M2_HOME}org/apache/camel/camel-core/2.8.0/camel-core-2.8.0.jar:\
${M2_HOME}org/apache/camel/camel-jms/2.8.0/camel-jms-2.8.0.jar:\
${M2_HOME}org/apache/camel/camel-spring/2.8.0/camel-spring-2.8.0.jar:\
${M2_HOME}commons-io/commons-io/2.1/commons-io-2.1.jar:\
${M2_HOME}commons-logging/commons-logging/1.0.3/commons-logging-1.0.3.jar:\
${M2_HOME}commons-logging/commons-logging-api/1.1/commons-logging-api-1.1.jar:\
${M2_HOME}org/fusesource/commonman/commons-management/1.0/commons-management-1.0.jar:\
${M2_HOME}commons-pool/commons-pool/1.4/commons-pool-1.4.jar:\
${M2_HOME}org/apache/geronimo/specs/geronimo-j2ee-management_1.0_spec/1.0/geronimo-j2ee-management_1.0_spec-1.0.jar:\
${M2_HOME}org/apache/geronimo/specs/geronimo-jms_1.1_spec/1.1.1/geronimo-jms_1.1_spec-1.1.1.jar:\
${M2_HOME}org/apache/geronimo/specs/geronimo-jta_1.0.1B_spec/1.0.1/geronimo-jta_1.0.1B_spec-1.0.1.jar:\
${M2_HOME}org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:\
${M2_HOME}org/springframework/spring-aop/3.0.5.RELEASE/spring-aop-3.0.5.RELEASE.jar:\
${M2_HOME}org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar:\
${M2_HOME}org/springframework/spring-beans/3.0.5.RELEASE/spring-beans-3.0.5.RELEASE.jar:\
${M2_HOME}org/springframework/spring-context/3.0.5.RELEASE/spring-context-3.0.5.RELEASE.jar:\
${M2_HOME}org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar:\
${M2_HOME}org/springframework/spring-expression/3.0.5.RELEASE/spring-expression-3.0.5.RELEASE.jar:\
${M2_HOME}org/springframework/spring-jms/3.0.5.RELEASE/spring-jms-3.0.5.RELEASE.jar:\
${M2_HOME}org/springframework/spring-tx/3.0.5.RELEASE/spring-tx-3.0.5.RELEASE.jar:\
${JAVA_HOME}lib/tools.jar:\
${M2_HOME}org/apache/xbean/xbean-spring/3.6/xbean-spring-3.6.jar:\
${M2_HOME}xpp3/xpp3_min/1.1.4c/xpp3_min-1.1.4c.jar:\
${M2_HOME}com/thoughtworks/xstream/xstream/1.3.1/xstream-1.3.1.jar" \
jmsclient.JMSClient $1 $2 $3 $4 $5
