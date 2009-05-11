set SPACE_NAME=Blitz_JavaSpace

set PATH="C:\jdk1.6\bin"
set JINI_LIB="C:/IvoryCloud/jini2_1/lib/"

set CP=%JINI_LIB%/jsk-lib.jar;lib/dashboard.jar;lib/stats.jar;thirdpartylib/backport-util-concurrent50.jar;%JINI_LIB%/jsk-platform.jar;%JINI_LIB%/sun-util.jar
set POLICY=-Djava.security.policy=policy/policy.all

java %POLICY% -cp %CP% org.dancres.blitz.tools.dash.StartDashBoard %SPACE_NAME%
