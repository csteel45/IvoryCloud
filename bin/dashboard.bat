@echo off

set SPACE_NAME=Blitz_JavaSpace

set LIB=..\lib


set JINI_LIB=..\lib\apache-river\lib


set CP=%JINI_LIB%\jsk-lib.jar;%LIB%\dashboard.jar;%LIB%\stats.jar;%JINI_LIB%\jsk-platform.jar;%JINI_LIB%\outrigger.jar;%LIB%\cloudbase.jar

set POLICY=-Djava.security.policy=..\policy\policy.all



start javaw %POLICY% -cp %CP% org.dancres.blitz.tools.dash.StartDashBoard %SPACE_NAME%