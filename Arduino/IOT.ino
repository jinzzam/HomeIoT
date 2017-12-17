#include <DHT.h>
#include<time.h>
#define DHTTYPE DHT11
#define BTN_PIN 7
#define LED 13
#define DHT_PIN 3
#define WATER A2
#define THIEFDETECTER 6
#define FANA 9
#define FANB 10

long nowTime;
int value;
int fan;
DHT dht(DHT_PIN, DHTTYPE);

bool state = HIGH;
bool tCheck = false;
int btn;
int h;
int t;

void setup() {
  Serial.begin(9600);
  pinMode(BTN_PIN, INPUT_PULLUP);
  pinMode(LED, OUTPUT);
  pinMode(WATER, INPUT);
  pinMode(THIEFDETECTER, INPUT);
  pinMode(FANA, OUTPUT);
  pinMode(FANB, OUTPUT);
}

void loop() {
  delay(1000);
//  Serial.println("NOW zzollang's room is... : ");

  //자동onoff선풍기 START
  t = dht.readTemperature();
  Serial.println(t);

  /*if (t >= 22) {
    analogWrite(FANA, 0);
    analogWrite(FANB, 255);
  }
  else {
    analogWrite(FANA, 0);
    analogWrite(FANB, 0);
  }*/
  //자동onoff선풍기 END

  //물재난 알림 STRAT
  int wt = analogRead(WATER);
  Serial.println(wt);
  if (wt >= 45) {
//    Serial.println("방 안에 물이 넘칩니다");
  }
  //물재난 알림 END


  //도둑알림 START


  int th = digitalRead(THIEFDETECTER);
  Serial.println(th);
  if (th == 1 && millis() / 1000 - nowTime >= 20) { //적외선센서가 사물을 인식했다면
    nowTime = millis() / 1000;
    while (millis() / 1000 - nowTime < 10) {
      btn = digitalRead(BTN_PIN);
//      Serial.println(btn);
      if (btn == 0) {  //버튼이 눌렸다면
        digitalWrite(LED, HIGH);
        tCheck = true;
        Serial.println(0);
        break;
      }
    }
    if (!tCheck) { //버튼이 눌리지 않았다면
      state = !state;
      digitalWrite(LED, state);
      Serial.println(1);
      Serial.println(1);
//      Serial.println("경고 : 도둑으로 의심되는 인물이 침입하였습니다.");
    }
    else {
      Serial.println(0);
      tCheck = false;
    }
  }
  else{
    Serial.println(1);
    Serial.println(0);
  }

  delay(100);

  //도둑알림 END
  

  //원격조종
  if (Serial.available()) {
    value = Serial.read();
    if (value == 'o') {
      digitalWrite(LED, HIGH);
    }
    else if (value == 'f') {
      digitalWrite(LED, LOW);
    }
    if (value == 'q') {
      digitalWrite(FANA, HIGH);
      digitalWrite(FANB, LOW);
    }
    else if (value == 'w') {
      digitalWrite(FANA, LOW);
      digitalWrite(FANB, LOW);
    }
    delay(100);
  }
  Serial.print("!");
}
