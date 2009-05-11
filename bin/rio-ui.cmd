@echo off

rem
rem Launches the Rio UI
rem

title Rio UI
set command_line=%*

java -jar "../lib/rio-ui.jar" %command_line%

