@echo off

rem This script provides the command and control utility for starting
rem Rio services and the Rio command line interface

rem Use local variables
setlocal

rem Set local variables
set JAVACMD=java

if "%RIO_HOME%" == "" set RIO_HOME=%~dp0..
REM if "%RIO_HOME%" == "" set RIO_HOME=.
set POLICY=-Djava.security.policy="%RIO_HOME%\policy\policy.all"

:noJiniHome
set JINI_HOME=%RIO_HOME%\lib\apache-river
goto haveJiniHome

:jiniNotFound
echo Cannot locate expected Jini (River) distribution, either set JINI_HOME or download Rio with dependencies, exiting
goto exitWithError

:haveJiniHome
set JINI_LIB=%JINI_HOME%\lib

if "%JAVA_MEM_OPTIONS%" == "" set JAVA_MEM_OPTIONS=-Xms256m -Xmx256m

rem Parse command line
if "%1"=="" goto interactive
if "%1"=="start" goto start

:interactive
rem echo "RIO interactive"
rem set cliExt="config\rio_cli.config
rem set cliExt=""
set command_line=%*
set launchTarget=org.rioproject.tools.cli.CLI
set classpath=-cp "%RIO_HOME%\lib\rio-cli.jar;%RIO_HOME%\lib\jsk-lib.jar;%JINI_HOME%\lib\jsk-platform.jar;%RIO_HOME%\lib\spring.jar;%RIO_HOME%\lib\commons-logging.jar;.\lib\groovy-all-1.6-RC-1.jar"
set props=-DRIO_HOME="%RIO_HOME%" -DJINI_HOME="%JINI_HOME%"
echo %JAVACMD% %classpath% -Xms256m -Xmx256m %props% %POLICY% %launchTarget% %cliExt% %command_line%
    
%JAVACMD% ^
    -server -Xms256m -Xmx256m ^
    %classpath% ^
    %props% ^
    %POLICY% ^
    %launchTarget% ^
    %cliExt% ^
    %command_line%
goto end

:start

echo "RIO start"
rem Get the service starter
shift
if "%1"=="" goto noService
set starterConfig="%RIO_HOME%\config\start-%1.config"
if not exist "%starterConfig%" goto noStarter
shift

echo "starter config [%starterConfig%]"
REM set RIO_LOG_DIR=%RIO_HOME%\logs\
set RIO_LOG_DIR=..\logs\
REM set RIO_NATIVE_DIR=%RIO_HOME%\lib\native
set RIO_NATIVE_DIR=..\lib\native

set classpath=-cp "%RIO_HOME%\lib\rio.jar;%RIO_HOME%\lib\boot.jar;%JINI_HOME%\lib\start.jar;%JAVA_HOME%\lib\tools.jar"
set agentpath=-javaagent:"%RIO_HOME%\lib\boot.jar"

set launchTarget=com.sun.jini.start.ServiceStarter
echo "HERE"
set STARTCMD=%JAVACMD% ^
    -server %JAVA_MEM_OPTIONS%  ^
    %classpath% ^
    %agentpath% ^
    %POLICY% ^
    -Djava.protocol.handler.pkgs=net.jini.url ^
    -Djava.library.path=%RIO_NATIVE_DIR% ^
    -DJINI_HOME="%JINI_HOME%" ^
    -DRIO_HOME="%RIO_HOME%" ^
    -Dorg.rioproject.home="%RIO_HOME%" ^
    -DRIO_NATIVE_DIR=%RIO_NATIVE_DIR% ^
    -DRIO_LOG_DIR=%RIO_LOG_DIR% ^
    %launchTarget% ^
    %starterConfig%
echo %STARTCMD%
%STARTCMD%
goto end

:noStarter
echo Cannot locate expected service starter file [start-%1.config] in [%RIO_HOME%\config], exiting"
goto exitWithError

:noService
echo "A service to start is required, exiting"

:exitWithError
exit /B 1

:end
endlocal
title Command Prompt
if "%OS%"=="Windows_NT" @endlocal
if "%OS%"=="WINNT" @endlocal
exit /B 0


REM java.exe -cp lib\rio-cli.jar;lib\jsk-lib.jar;lib\apache-river\lib\jsk-platform.jar;lib\spring.jar;lib\commons-logging.jar;lib\groovy-all-1.6-RC-1.jar -Xms256m -Xmx256m -DRIO_HOME=. -DJINI_HOME=.\lib\apache-river -Djava.security.policy=policy\policy.all org.rioproject.tools.cli.CLI  undeploy src/main/conf/tradinggrid.xml -iu httpRoots=./deploy:./lib:./lib/apache-river/lib-dl -t=5000 discoveryTimeout=10000 groups=all
REM java.exe -cp .\lib\rio-cli.jar;.\lib\jsk-lib.jar;.\lib\apache-river\lib\jsk-platform.jar;.\lib\spring.jar;.\lib\commons-logging.jar;.\lib\groovy-all-1.6-RC-1.jar      -Xms256m -Xmx256m -DRIO_HOME=. -DJINI_HOME=.\lib\apache-river -Djava.security.policy=policy\policy.all org.rioproject.tools.cli.CLI  undeploy src/main/conf/tradinggrid.xml -iu httpRoots=./deploy:./lib:./lib/apache-river/lib-dl -t=5000 discoveryTimeout=10000 groups=all
REM java.exe -cp .\lib\rio-cli.jar;.\lib\jsk-lib.jar;.\lib\apache-river\lib\jsk-platform.jar;.\lib\spring.jar;.\lib\commons-logging.jar;.\lib\groovy-all-1.6-RC-1.jar      -Xms256m -Xmx256m -DRIO_HOME=. -DJINI_HOME=.\lib\apache-river -Djava.security.policy=policy\policy.all org.rioproject.tools.cli.CLI  undeploy src/main/conf/tradinggrid.xml -iu httpRoots=./deploy:./lib:./lib/apache-river/lib-dl -t=5000 discoveryTimeout=10000 groups=all

REM java.exe -cp .\lib\rio-cli.jar;.\lib\jsk-lib.jar;.\lib\apache-river\lib\jsk-platform.jar;.\lib\spring.jar;.\lib\commons-logging.jar;.\lib\groovy-all-1.6-RC-1.jar -Xms256m -Xmx256m -DRIO_HOME=. -DJINI_HOME=.\lib\apache-river -Djava.security.policy=policy\policy.all org.rioproject.tools.cli.CLI  undeploy src/main/conf/tradinggrid.xml -iu httpRoots=./deploy:./lib:./lib/apache-river/lib-dl -t=5000 discoveryTimeout=10000 groups=all
