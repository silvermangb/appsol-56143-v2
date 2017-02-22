#!/bin/bash

rm -f /opt/SYMCnbappgui/appliance.war /opt/SYMCnbappws/appliancews.war
cp appliance.war /opt/SYMCnbappgui/appliance.war
cp appliancews.war /opt/SYMCnbappws/appliancews.war
 
 
 
# For NBA after 3.0
rm -f /opt/apache-tomcat/vxos/webapps/appliance.war /opt/apache-tomcat/vxos/webapps/appliancews.war
ln -s /opt/SYMCnbappgui/appliance.war /opt/apache-tomcat/vxos/webapps/appliance.war
ln -s /opt/SYMCnbappws/appliancews.war /opt/apache-tomcat/vxos/webapps/appliancews.war
 
# Restart services
service tomcat-vxos restart
