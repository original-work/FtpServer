# java.sh writen by zouyj

# environment for java
if [ -d /usr/java/jdk1.6.0_20 ]; then
	export JAVA_HOME=/usr/java/jdk1.6.0_20
fi

add_jar()
{
	[ -e $1 ] && CLASSPATH=$CLASSPATH:$1
}

if [ $JAVA_HOME ]; then
	[ $PATH ] && PATH=$JAVA_HOME/bin:$PATH:/usr/FtpServer/bin || PATH=$JAVA_HOME/bin:/usr/FtpServer/bin
	[ $CLASSPATH ] && CLASSPATH=.:$CLASSPATH || CLASSPATH=.
	add_jar $JAVA_HOME/lib/dt.jar
	add_jar $JAVA_HOME/lib/tools.jar
	add_jar /usr/FtpServer/lib/FtpServer.jar
	add_jar /usr/FtpServer/lib/aspectjrt.jar
	add_jar /usr/FtpServer/lib/aspectjweaver.jar
	add_jar /usr/FtpServer/lib/backport-util-concurrent-2.1.jar
	add_jar /usr/FtpServer/lib/commons-collections.jar
	add_jar /usr/FtpServer/lib/commons-dbcp.jar
	add_jar /usr/FtpServer/lib/commons-lang.jar
	add_jar /usr/FtpServer/lib/commons-logging.jar
	add_jar /usr/FtpServer/lib/commons-net-2.2.jar
	add_jar /usr/FtpServer/lib/commons-pool.jar
	add_jar /usr/FtpServer/lib/hsqldb.jar
	add_jar /usr/FtpServer/lib/log4j-1.2.14.jar
	add_jar /usr/FtpServer/lib/mysql-connector-java-5.0.8-bin.jar
	add_jar /usr/FtpServer/lib/quartz-1.5.2.jar
	add_jar /usr/FtpServer/lib/spring.jar
	add_jar /usr/FtpServer/lib/spring-mock.jar
	export CLASSPATH
fi

