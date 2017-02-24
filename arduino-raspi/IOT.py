
"""
Temperature/Humidity monitor using Raspberry Pi and Arduino.
Data is displayed at thingspeak.com
"""
import time
import sys
import RPi.GPIO as GPIO
from time import sleep
import Adafruit_DHT
import urllib2

import serial

ser = serial.Serial('/dev/ttyACM0', 9600)
key = 'W8LUI7XUVMMF39UB'
tempAndHum = [0,0, 0.0]
time.sleep(60)

#Tests if 's' is int
def isInt(s):
    try:
        int(s)
        return True
    except ValueError:
        return False
    
#Tests if 's' is float
def isFloat(s):
    try:
        float(s)
        return True
    except ValueError:
        return False
    
#Test if list[index] contains a value <- Currently not in use
def containsValue(index):
    try:
        v = tempAndHum[index]
        return True
    except IndexError:
        return False


# main() function
def main():
    # use sys.argv if needed    
    baseURL = 'https://api.thingspeak.com/update?api_key=%s' % key
    #sys.argv[1]
    oldTempAndHum = [0,0,0]
    while True:
        
        ser.flushInput()
        data = ser.readline()[:-2]

        
        tempAndHum = [x.strip() for x in data.split(',')]
        print tempAndHum

        #Test the lenght of received data, so that there will not be empty indexes (set the last temp/hum if its empty)
        if len(tempAndHum) < 1:
                tempAndHum = [oldTempAndHum[0], oldTempAndHum[1], oldTempAndHum[2]]
        elif len(tempAndHum) < 2:
                tempAndHum = [tempAndHum[0], oldTempAndHum[1], oldTempAndHum[2]]
        elif len(tempAndHum) < 3:
                tempAndHum = [tempAndHum[0], tempAndHum[1], oldTempAndHum[2]]
 
        #If received temperature/humidity is not int, set the last temperature/humidity as the new temperature/humidity

        if not isInt(tempAndHum[0]):
            temperature = oldTempAndHum[0]
            print("NOT INTEGER ERROR temperature set to old temperature")
        else:
            temperature = tempAndHum[0]
 
        
 
        if not isInt(tempAndHum[1]):
            humidity = oldTempAndHum[1]
            print("NOT INTEGER ERROR humidity set to old humidity")
        else:
            humidity = tempAndHum[1]
  

 
        if not isFloat(tempAndHum[2]):
            airQuality = oldTempAndHum[2]
            print("NOT FLOAT ERROR airQuality set to old airQuality")
        else:
            airQuality= tempAndHum[2]

            
        #Only send data when hum or temp is different from last
        if oldTempAndHum != tempAndHum:                
            f = urllib2.urlopen(baseURL +"&field1=%s&field2=%s&field3=%s" % (temperature, humidity, airQuality))
            print ("DATA SENT")
            oldTempAndHum = tempAndHum
            f.close()
        else:    
            print ("NO CHANGES")
        sleep(15)
                

# call main
if __name__ == '__main__':
    main()
