@echo off

title httpd port 8082
set PATH="C:\jdk1.6\bin"
set JINI_HOME="C:/IvoryCloud/jini2_1/lib/"

java -jar %JINI_HOME%tools.jar -dir lib -port 8082 -verbose
