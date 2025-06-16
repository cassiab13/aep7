#include <Ultrasonic.h>

#define pino_trigger 4
#define pino_echo 5

Ultrasonic ultrasonic(pino_trigger, pino_echo);

String cpf = "";
int contador = 0;
bool contando = false;

void setup() {
  Serial.begin(9600);
  Serial.println("Aguardando CPF para iniciar a contagem...");
}

void loop() {
  if (Serial.available()) {
    String entrada = Serial.readStringUntil('\n');
    entrada.trim();    

    if (entrada == "STOP" && contando) {
        contando = false;
        Serial.print("CPF: ");
        Serial.print(cpf);
        Serial.print(" - Garrafas: ");
        Serial.println(contador);
        cpf = "";
        contador = 0;
    } else if (!contando) {
      
        cpf = entrada;
        contando = true;
        contador = 0;
        Serial.println("CPF recebido: " + cpf);
        Serial.println("Contagem iniciada.");
    }
  }

  if (contando) {
    long microsec = ultrasonic.timing();
    float distancia_cm = ultrasonic.convert(microsec, Ultrasonic::CM);

    if (distancia_cm <= 10.0) {
      contador++;
      delay(600);
    }
  }

  delay(200);
}
