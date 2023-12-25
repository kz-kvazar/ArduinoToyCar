#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>

const char *ssid = "Машинка";
const char *password = "password";

const int button0Pin = 4;  // Номер пина для кнопки 0
const int button1Pin = 5;  // Номер пина для кнопки 1
const int button2Pin = 12;  // Номер пина для кнопки 2
const int button3Pin = 14;  // Номер пина для кнопки 3

bool button0State = false;
bool button1State = false;
bool button2State = false;
bool button3State = false;

uint32_t hourTime = 0;

ESP8266WebServer server(80);

void setup() {
  Serial.begin(9600);

  // Установка точки доступа
  WiFi.softAP(ssid, password);

  // Вывод IP-адреса точки доступа
  Serial.print("IP Address: ");
  Serial.println(WiFi.softAPIP());

  // Настройка пинов кнопок
  pinMode(button0Pin, OUTPUT);
  pinMode(button1Pin, OUTPUT);
  pinMode(button2Pin, OUTPUT);
  pinMode(button3Pin, OUTPUT);

  digitalWrite(button0Pin, HIGH);
  digitalWrite(button1Pin, LOW);
  digitalWrite(button2Pin, LOW);
  digitalWrite(button3Pin, LOW);

  // Обработчики для веб-интерфейса для мобидьной версии
  server.on("/", HTTP_GET, handleRoot);
  server.on("/button0", HTTP_GET, handleButton0);
  server.on("/button1", HTTP_GET, handleButton1);
  server.on("/button2", HTTP_GET, handleButton2);
  server.on("/button3", HTTP_GET, handleButton3);

  // Обработка вэб интерфейса для приложения
  server.on("/forwardBtnDown", HTTP_GET, forwardBtnDown);
  server.on("/forwardBtnUp", HTTP_GET, forwardBtnUp);

  server.on("/backBtnDown", HTTP_GET, backBtnDown);
  server.on("/backBtnUp", HTTP_GET, backBtnUp);

  server.on("/leftBtnDown", HTTP_GET, leftBtnDown);
  server.on("/leftBtnUp", HTTP_GET, leftBtnUp);

  server.on("/rightBtnDown", HTTP_GET, rightBtnDown);
  server.on("/rightBtnUp", HTTP_GET, rightBtnUp);


  // Запуск веб-сервера
  server.begin();
}

void loop() {
  // Обработка HTTP-запросов
  server.handleClient();

  if (millis() - hourTime > 120000) {
    digitalWrite(button0Pin, LOW);
    digitalWrite(button1Pin, LOW);
    digitalWrite(button2Pin, LOW);
    digitalWrite(button3Pin, LOW);
    hourTime = millis();
  }
}

void handleRoot() {
  String html = "<html><head><style>";
  html += "body { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; margin: 0; }";
  html += "button { width: 300px; height: 150px; margin: 5px; }";
  html += "</style></head><body>";
  html += "<button id='btn0' onclick='sendRequest(\"/button0\")' style='background-color:";
  html += button0State ? "green" : "white";
  html += "'>Button 0</button><br>";
  html += "<div style='display: flex;'>";
  html += "<button id='btn1' onclick='sendRequest(\"/button1\")' style='background-color:";
  html += button1State ? "green" : "white";
  html += "'>Button 1</button>";
  html += "<button id='btn2' onclick='sendRequest(\"/button2\")' style='background-color:";
  html += button2State ? "green" : "white";
  html += "'>Button 2</button></div><br>";
  html += "<button id='btn3' onclick='sendRequest(\"/button3\")' style='background-color:";
  html += button3State ? "green" : "white";
  html += "'>Button 3</button><br>";
  html += "<script>function sendRequest(url) {";
  html += "var buttonId = url.substring(7);";
  html += "var button = document.getElementById('btn' + buttonId);";
  html += "var xhr = new XMLHttpRequest();";
  html += "xhr.onreadystatechange = function() {";
  html += "  if (xhr.readyState == 4 && xhr.status == 200) {";
  html += "    button.style.backgroundColor = xhr.responseText === 'true' ? 'green' : 'white';";
  html += "  }";
  html += "};";
  html += "xhr.open('GET', url, true);";
  html += "xhr.send();";
  html += "}</script>";
  html += "</body></html>";
  server.send(200, "text/html", html);
}
void forwardBtnDown(){
  digitalWrite(button3Pin, LOW);
  digitalWrite(button0Pin, HIGH);
  button0State = true;
  button3State = false;
  Serial.println("go forward...");
  server.send(200, "text/plain", "OK");
}
void forwardBtnUp(){
  digitalWrite(button3Pin, LOW);
  digitalWrite(button0Pin, LOW);
  button0State = false;
  button3State = false;
  Serial.println("stop forward...");
  server.send(200, "text/plain", "OK");
}
void backBtnDown(){
  digitalWrite(button0Pin, LOW);
  digitalWrite(button3Pin, HIGH);
  button3State = true;
  button0State = false;
  Serial.println("go back...");
  server.send(200, "text/plain", "OK");
}
void backBtnUp(){
  digitalWrite(button0Pin, LOW);
  digitalWrite(button3Pin, LOW);
  button3State = false;
  button0State = false;
  Serial.println("stop back...");
  server.send(200, "text/plain", "OK");
}
void leftBtnDown(){
  digitalWrite(button2Pin, LOW);
  digitalWrite(button1Pin, HIGH);
  button1State = true;
  button2State = false;
  Serial.println("go left...");
  server.send(200, "text/plain", "OK");
}
void leftBtnUp(){
  digitalWrite(button2Pin, LOW);
  digitalWrite(button1Pin, LOW);
  button1State = false;
  button2State = false;
  Serial.println("stop left...");
  server.send(200, "text/plain", "OK");
}
void rightBtnDown(){
  digitalWrite(button1Pin, LOW);
  digitalWrite(button2Pin, HIGH);
  button2State = true;
  button1State = false;
  Serial.println("go right...");
  server.send(200, "text/plain", "OK");
}
void rightBtnUp(){
  digitalWrite(button1Pin, LOW);
  digitalWrite(button2Pin, LOW);
  button2State = false;
  button1State = false;
  Serial.println("stop right...");
  server.send(200, "text/plain", "OK");
}

void handleButton0() {
  button0State = !button0State;
  digitalWrite(button3Pin, LOW);
  button3State = false;
  delay(100);
  digitalWrite(button0Pin, button0State ? HIGH : LOW);
  server.send(200, "text/plain", String(button0State));
}

void handleButton1() {
  button1State = !button1State;
  digitalWrite(button2Pin, LOW);
  button2State = false;
  delay(100);
  digitalWrite(button1Pin, button1State ? HIGH : LOW);
  server.send(200, "text/plain", String(button1State));
}

void handleButton2() {
  button2State = !button2State;
  digitalWrite(button1Pin, LOW);
  button1State = false;
  delay(100);
  digitalWrite(button2Pin, button2State ? HIGH : LOW);
  server.send(200, "text/plain", String(button2State));
}

void handleButton3() {
  button3State = !button3State;
  digitalWrite(button0Pin, LOW);
  button0State = false;
  delay(100);
  digitalWrite(button3Pin, button3State ? HIGH : LOW);
  server.send(200, "text/plain", String(button3State));
}

void stopMotor(){
  digitalWrite(button0Pin, LOW);
  button0State = false;
  digitalWrite(button1Pin, LOW);
  button1State = false;
  digitalWrite(button2Pin, LOW);
  button2State = false;
  digitalWrite(button3Pin, LOW);
  button3State = false;
  delay(100);
}
