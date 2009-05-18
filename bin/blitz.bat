@echo off

rem set PATH="C:\jdk1.6\bin"
set JINI_HOME="../lib/apache-river/lib/"
set START_CONFIG=config/start-blitz.config

java -Djava.security.policy=policy/policy.all -jar %JINI_HOME%/start.jar %START_CONFIG%
