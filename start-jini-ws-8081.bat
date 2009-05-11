@echo off

title httpd port 8081
set PATH="C:\jdk1.6\bin"
set JINI_HOME="C:/IvoryCloud/jini2_1/lib/"

java -jar %JINI_HOME%tools.jar -dir C:/IvoryCloud/jini2_1/lib-dl/ -port 8081 -verbose
