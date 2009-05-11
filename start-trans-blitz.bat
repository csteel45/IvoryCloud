@echo off

set JINI_HOME="C:/IvoryCloud/lib/apache-river/lib"
set START_CONFIG=config/start-trans-blitz.config

java -Djava.security.policy=policy/policy.all -jar %JINI_HOME%/start.jar %START_CONFIG%
