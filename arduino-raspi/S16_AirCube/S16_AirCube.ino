
#include <OneWire.h>
#include <DallasTemperature.h>

#define ONE_WIRE_BUS 2
#define PWM_PIN 5

OneWire oneWire(ONE_WIRE_BUS);

DallasTemperature sensors(&oneWire);

int dataArray[125];
int maxVal, dataValue = 0;
float volts = 0;

const int MQ_CALIB = 1000;

int MQ135 = A1;
double AirQuality;
float humidity;
int temperature;

void setup() {
  Serial.begin(9600);
  analogWrite(PWM_PIN, 128); //Annetaan 980Hz PWM pinniin 5
  pinMode(9, OUTPUT); // Tehdään pinnistä 9 output 5V

  sensors.begin();
}

void loop() {
  digitalWrite(9, HIGH);
  /*****************Temperature START***********************/
  sensors.requestTemperatures();
  temperature = sensors.getTempCByIndex(0);
  Serial.print(temperature);
  Serial.print(",");
  /*****************Temperature END************************/

  /******************Humidity START************************/
  for (int i = 0; i < 125; i++) {
    dataArray[i] = analogRead(A0);
  }
  maxVal = dataArray[0];
  for (int i = 0; i < 125 ; i++) {
    if (maxVal < dataArray[i + 1]) {
      maxVal = dataArray[i + 1];
    }
  }
  dataValue = maxVal;
  volts = dataValue / 204.6;
  humidity = 0.3518 * (pow(volts, 4)) - 6.5091 * (pow(volts, 3)) + 41.327 * (pow(volts, 2)) - 117 * volts + 176.19;

  Serial.print((int)humidity);
  Serial.print(",");
  /******************Humidity END**************************/


  /******************Air Quality START**************************/
  
  AirQuality = analogRead(MQ135);
  Serial.println(AirQuality / MQ_CALIB);

  /*******************Air Quality END***************************/
  delay(100);
}
