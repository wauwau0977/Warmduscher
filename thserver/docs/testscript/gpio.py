import RPi.GPIO as GPIO
import time
GPIO.setmode(GPIO.BOARD)
GPIO.setwarnings(False)
GPIO.setup(36,GPIO.OUT)
GPIO.setup(32,GPIO.OUT)


print "Power Pin 32 ON. Wait 10 seconds"
GPIO.output(32,GPIO.HIGH)
time.sleep(10)
print "Relay on"
GPIO.output(36,GPIO.HIGH)
time.sleep(5)
print "Relay off"
GPIO.output(36,GPIO.LOW)
time.sleep(30)

GPIO.output(32,GPIO.LOW)