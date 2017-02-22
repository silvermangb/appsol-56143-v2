#!/bin/bash

rm -f /opt/SYMCnbappgui/appliance.war /opt/SYMCnbappws/appliancews.war
cp appliance.war /opt/SYMCnbappgui/appliance.war
cp appliancews.war /opt/SYMCnbappws/appliancews.war
 
 
# For NBA earlier than NBA3.0.1
rm -f /opt/SYMCnbappws/webserver/webapps/appliance.war /opt/SYMCnbappws/webserver/webapps/appliancews.war
ln -s /opt/SYMCnbappgui/appliance.war /opt/SYMCnbappws/webserver/webapps/appliance.war
ln -s /opt/SYMCnbappws/appliancews.war /opt/SYMCnbappws/webserver/webapps/appliancews.war
 
# Restart services
/opt/apache-tomcat/vrts/scripts/tomcat_instance.py stop  --all
/opt/apache-tomcat/vrts/scripts/tomcat_instance.py start --all

