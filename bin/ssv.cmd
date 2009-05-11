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

rem This script starts the Service Statement Viewer

if exist envcheck.cmd call envcheck.cmd

if errorlevel 1 goto end

setlocal
set classpath=-cp ..\lib\ssv.jar;..\lib\rio.jar;..\lib\apache-river\lib\jsk-lib.jar;..\lib\apache-river\lib\jsk-platform.jar;
set launchTarget=org.rioproject.utilities.ssv.ServiceStatementViewer

java -server %classpath% -Djava.protocol.handler.pkgs=net.jini.url %launchTarget% ..\configs\utils.config

endlocal
:end
