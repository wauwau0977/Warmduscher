#!/bin/bash

while :
do
  
	# if nc -zw1 google.com 443; then
	#if nc -zw1 192.168.1.1 80; then
	if nc -zw1 google.com 443; then
		echo `date` "WifiDropCheck: OK Connection fine"
		
	else
		echo -e "\n"
		echo `date` "WifiDropCheck: FAILED Internet failed"
		
		echo "Restarting wlan0 network driver, step 1"
		sudo /usr/sbin/rmmod brcmfmac
		
		echo "Restarting wlan0 network driver, step 2"
		sudo /usr/sbin/modprobe brcmfmac roamoff=1
		
		echo "try wlan down"
		sudo ifconfig wlan0 down
		
		echo "try wlan up"
		sudo ifconfig wlan0 up

		# seems as recovery does require multiple iterations?!
		# echo -e "completed. Now doing some extra sleep";
		# sleep 180
		echo -e `date` "done"
		
	fi
  
	sleep 60
	
done
