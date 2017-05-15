@echo off
set java_home=%JAVA_HOME%
set ant_home=%ANT_HOME%
set path=%java_home%\bin;%ant_home%\bin;%path%
set classpath=%JAVA_HOME%\LIB\dt.jar;.;%JAVA_HOME%\LIB\tools.jar
call ant
echo compile complete
pause
