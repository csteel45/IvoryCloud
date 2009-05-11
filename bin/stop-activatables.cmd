@echo off

rem
rem Stop running activatable processes
rem

if "%JAVA_HOME%" == "" goto noJavaHome
if "%JINI_HOME%" == "" goto noJiniHome
if "%RIO_HOME%" == "" goto noRioHome
goto startWebster

:noJavaHome
echo Set the JAVA_HOME environment variable to point to the location where you have installed Java
goto exitWithError

:noJiniHome
echo Set the JINI_HOME environment variable to point to the location where you have installed Jini
goto exitWithError

:noRioHome
echo Set the RIO_HOME environment variable to point to the location where you have installed Rio
goto exitWithError

set RIO_LOG_DIR="%RIO_HOME%"\logs\

"%JAVA_HOME%\bin\java" -server -jar ^
    -Djava.security.policy=%RIO_HOME%\policy\policy.all ^
    -DRIO_HOME=%RIO_HOME% ^
    -DJINI_HOME=%JINI_HOME% ^
    -DRIO_LOG_DIR=%RIO_LOG_DIR% ^
    %JINI_HOME%\lib\destroy.jar %RIO_HOME%\config\stop-activatables.config

exit /B 0

:exitWithError
exit /B 1
