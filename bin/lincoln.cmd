
REM This script starts a non-persistent Lincoln using the start-lincoln.config configuration file found in
REM $RIO_UTILS/configs.

REM Check environment
if "%JAVA_HOME%" == "" goto noJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
set JAVACMD=%JAVA_HOME%\bin\java.exe
goto endOfJavaHome

:noJavaHome
set JAVACMD=java.exe
:endOfJavaHome  

rem Set local variables
if "%RIO_HOME%" == "" set RIO_HOME=%~dp0..
if "%JINI_HOME%" == "" set JINI_HOME=%RIO_HOME%\jini2_1
goto haveJiniHome

:noJiniHome
if not exist "%RIO_HOME%\lib\jini2_1" goto jiniNotFound
set JINI_HOME=%RIO_HOME%\lib\jini2_1
goto haveJiniHome

:jiniNotFound
echo Cannot locate expected Jini distribution, either set JINI_HOME or download Rio with dependencies, exiting
goto exitWithError

:haveJiniHome
set JINI_LIB=%JINI_HOME%\lib

set classpath=%RIO_HOME%\lib\boot.jar;%JINI_HOME%\lib\start.jar
set launchTarget=com.sun.jini.start.ServiceStarter

%JAVACMD% -cp %classpath% -Djava.security.policy=%RIO_HOME%\policy\policy.all -Djava.protocol.handler.pkgs=net.jini.url -DRIO_UTILS=%RIO_HOME% -DRIO_HOME=%RIO_HOME% -DJINI_HOME=%JINI_HOME% %launchTarget% %RIO_HOME%\config\start-lincoln.config 
