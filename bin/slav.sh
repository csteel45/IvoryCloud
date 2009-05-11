#!/bin/bash

#/*
# 
# Copyright 2005 Sun Microsystems, Inc.
# Copyright 2005 GigaSpaces, Inc.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
# 	http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# 
#*/

# This script stats the SLA Threshold Event Viewer

# Check environment
if [ -f "./envcheck.sh" ] ; then
    ./envcheck.sh
    if [ $? != "0" ] ; then
        exit $? 
    fi
fi

classpath="-cp $RIO_UTILS/lib/slav.jar:$RIO_HOME/lib/rio.jar:$JINI_HOME/lib/jsk-lib.jar:$JINI_HOME/lib/jsk-platform.jar"
launchTarget=org.jini.rio.utilities.slav.SLAEventViewer
config=$RIO_UTILS/configs/utils.config

opSys=`uname -s`
if [ $opSys = "Darwin" ] ; then
    $JAVA_HOME/bin/java -client $classpath -Djava.protocol.handler.pkgs=net.jini.url -Xdock:name="Rio SLAThresholdEvent Viewer" $launchTarget $config
else
    $JAVA_HOME/bin/java -client $classpath -Djava.protocol.handler.pkgs=net.jini.url $launchTarget $config
fi
