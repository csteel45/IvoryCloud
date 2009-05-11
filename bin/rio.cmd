@echo off

rem This script provides the command and control utility for starting
rem Rio services and the Rio command line interface

rem Use local variables
setlocal

rem Set local variables
if "%RIO_HOME%" == "" set RIO_HOME=%~dp0..
set RIO_LIB=%RIO_HOME%\lib
set POLICY=-Djava.security.policy=%RIO_HOME%\policy\policy.all
if "%JINI_HOME%" == "" goto noJiniHome
goto haveJiniHome

:noJiniHome
if not exist "%RIO_HOME%\lib\apache-river" goto jiniNotFound
set JINI_HOME=%RIO_HOME%\lib\apache-river
goto haveJiniHome

:jiniNotFound
echo Cannot locate expected Jini (River) distribution, either set JINI_HOME or download Rio with dependencies, exiting
goto exitWithError

:haveJiniHome
set JINI_LIB=%JINI_HOME%\lib

rem CJS if not exist %JAVA_HOME%\bin\java.exe goto noJavaHome
rem CJS set JAVACMD="%JAVA_HOME%\bin\java.exe"
set JAVACMD=java.exe
goto endOfJavaHome

:noJavaHome
set JAVACMD=java.exe
:endOfJavaHome  

if "%JAVA_MEM_OPTIONS%" == "" set JAVA_MEM_OPTIONS=-Xms256m -Xmx256m

rem Parse command line
if "%1"=="" goto interactive
if "%1"=="start" goto start

:interactive
echo "RIO interactive"
rem set cliExt="%RIO_HOME%\config\rio_cli.config"
set command_line=%*
set launchTarget=org.rioproject.tools.cli.CLI
set classpath=-cp "%RIO_LIB%\rio-cli.jar;%JINI_LIB%\jsk-lib.jar;%JINI_LIB%\jsk-platform.jar;%RIO_LIB%\spring\spring.jar;%RIO_LIB%\jakarta-commons\commons-logging.jar;%RIO_LIB%\groovy\groovy-all-1.6-RC-1.jar;%RIO_LIB%\blitz.jar;%RIO_LIB%\stats.jar"
set props=-DRIO_HOME=%RIO_HOME% -DJINI_HOME=%JINI_HOME%
set START_CMD=%JAVACMD% ^
	%JAVA_MEM_OPTIONS% ^
	%classpath% ^
	%props% ^
	%POLICY% ^
    %launchTarget% %command_line%
echo Start Command = %START_CMD%
%START_CMD%
goto end

:start

echo "RIO start"
rem Get the service starter
shift
if "%1"=="" goto noService
REM CJS set starterConfig=%RIO_HOME%\config\start-%1.config
set starterConfig=..\config\start-%1.config
if not exist "%starterConfig%" goto noStarter
shift

echo "starter config [%starterConfig%]"
set RIO_LOG_DIR=%RIO_HOME%\logs\
set RIO_NATIVE_DIR=%RIO_HOME%\lib\native

set classpath=-cp "%RIO_HOME%\lib\boot.jar;%JINI_HOME%\lib\start.jar;%JAVA_HOME%\lib\tools.jar;%RIO_HOME%\lib\blitz.jar;%RIO_HOME%\lib\stats.jar"
set agentpath=-javaagent:"%RIO_HOME%\lib\boot.jar"

set launchTarget=com.sun.jini.start.ServiceStarter

set START_CMD=%JAVACMD% ^
    %JAVA_MEM_OPTIONS%  ^
    %classpath% ^
    %agentpath% ^
    %POLICY% ^
    -Djava.protocol.handler.pkgs=net.jini.url ^
    -Djava.library.path="%RIO_NATIVE_DIR%" ^
    -DJINI_HOME=%JINI_HOME% ^
    -DRIO_HOME=%RIO_HOME% ^
    -Dorg.rioproject.home=%RIO_HOME% ^
    -DRIO_NATIVE_DIR=%RIO_NATIVE_DIR% ^
    -DRIO_LOG_DIR=%RIO_LOG_DIR% ^
    %launchTarget% %starterConfig%
    
echo %START_CMD%
%START_CMD%
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
