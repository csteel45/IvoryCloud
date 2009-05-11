@echo off

set JINI_HOME="C:/IvoryCloud/apache-river/lib/"
set START_CONFIG=config/start-reggie.config

java -Djava.security.policy=policy/policy.all -jar %JINI_HOME%/start.jar %START_CONFIG%
