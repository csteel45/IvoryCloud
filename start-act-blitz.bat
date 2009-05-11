@echo off

set PATH="C:\jdk1.6\bin"
set JINI_HOME="C:/IvoryCloud/jini2_1/lib/"
set START_CONFIG=config/start-act-blitz.config

java -Djava.security.policy=policy/policy.all -jar %JINI_HOME%/start.jar %START_CONFIG%
