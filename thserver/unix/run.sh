#! /bin/sh


###############################################################################
#
# OBSOLETE: USE SYSTEMD which is much better
#
###############################################################################



###############################################################################
# Raspeberry Sensor THServer
#
# Patrick Heusser, 4.3.2016
###############################################################################

# CRON expression:
# 0 18 * * * cd /home/pi/temperature && ./run.sh  >> /dev/null 2>&1


getPID() {
  echo `ps -ef | grep "$PROC_ID" | grep -v grep | awk '{print $2}'`
}

escape() {
  echo $1 | sed "s/@\|:\|\/\|\.\||\|'\|\\s/_/g"
}

PROC_ID=`escape "RASPBERRY_TH_SERVER"`


killProcessIfRequired() {
  echo "Check for an existing process identifier: " $PROC_ID
  if [ "$PROC_ID" = "" ]; then
  	echo "no identifier given. do not kill anything. exit";
  	return;
  fi

  process=$(getPID);
  if [ "$process" = "" ]; then
    echo "-- process is not running. Continue."
  else
    echo "-- WARNING: about to kill (send sig 15) the existing process: $process"
    kill -15 $process
    sleep 3
    process=$(getPID);
    if [ "$process" = "" ]; then
      echo "-- successfully killed."
    else
      echo "-- not yet killed. send kill (signal 9) now"
      kill -9 $process
      sleep 3
    fi
  fi
}

#echo "DO NOT RUN SINCE DB HAS NO SPACE"
#exit;


echo "Check if server is running"
killProcessIfRequired;
sleep 2

echo "Clean old logs"
LOG=nohup.out
rm -rf $LOG
find /tmp -name "*tomcat*" -mtime +5 -exec rm -rf {} \;
rm -f /tmp/spring*log*

echo "Start Server"
# add  -Dspring.profiles.active=default,dev for a dedicated profile
nohup java -D$PROC_ID -Djava.security.egd=file:///dev/urandom -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar thserver-0.0.1-SNAPSHOT.jar   &
sleep 1
chmod ugo+rw $LOG
tail -5000f $LOG
