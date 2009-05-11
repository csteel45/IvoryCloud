@echo off

@rem /*
@rem 
@rem Copyright 2005 Sun Microsystems, Inc.
@rem 
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem 
@rem 	http://www.apache.org/licenses/LICENSE-2.0
@rem 
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem 
@rem */

rem This script stats the SLA Threshold Event Viewer

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

setlocal
set classpath=-cp ..\lib\slav.jar;%RIO_HOME%\lib\rio.jar;%JINI_HOME%\lib\jsk-lib.jar;%JINI_HOME%\lib\jsk-platform.jar;
set launchTarget=org.jini.rio.utilities.slav.SLAEventViewer

"%JAVACMD%" %classpath% -Djava.protocol.handler.pkgs=net.jini.url %launchTarget% ..\config\utils.config

endlocal
:end
