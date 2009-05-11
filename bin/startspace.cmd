@echo off

title JavaSpace
set command_line=%*
java -Djava.security.policy=..\policy\policy.all -jar ..\lib\apache-river\lib\start.jar ..\config\start-persistent-outrigger.config
