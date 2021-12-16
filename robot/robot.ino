/*
 * Created by Manuel Díaz <yosoymacnolo@gmail.com>
 * Date: 06/11/2021
 */

#include <AFMotor.h>
#include <SoftwareSerial.h>

AF_DCMotor motorFR(1, MOTOR12_8KHZ);
AF_DCMotor motorFL(2, MOTOR12_8KHZ);
AF_DCMotor motorBL(3, MOTOR34_8KHZ);
AF_DCMotor motorBR(4, MOTOR34_8KHZ);

SoftwareSerial bt(9,10);

const int MAX_COMMAND_SIZE = 10;

void setup() {
  bt.begin(9600);
  
  motorFR.run(RELEASE);
  motorFL.run(RELEASE);
  motorBL.run(RELEASE);
  motorBR.run(RELEASE);
}

void loop() {
  if (bt.available()) {
    char input[MAX_COMMAND_SIZE + 1];
    byte size = bt.readBytes(input, MAX_COMMAND_SIZE);
    input[size] = 0;
    char* command = strtok(input, ";");
    while (command != 0) {
      char* separator = strchr(command, ':');
      if (separator != 0)
      {
          *separator = 0;
          int a = atoi(command);
          ++separator;
          int b = atoi(separator);
          apply(a, b);
      }
      command = strtok(0, ";");
    }
  } else {
    motorFR.run(RELEASE);
    motorBR.run(RELEASE);
    motorFL.run(RELEASE);
    motorBL.run(RELEASE);
  }
}

void apply(int a, int b) {
  motorFL.setSpeed(abs(a));
  motorBL.setSpeed(abs(a));
  motorFR.setSpeed(abs(b));
  motorBR.setSpeed(abs(b));

  if (a != 0) {
    if (a > 0) {
      motorFL.run(FORWARD);
      motorBL.run(FORWARD);
    } else if (a < 0) {
      motorFL.run(BACKWARD);
      motorBL.run(BACKWARD);
    }
  } else {
    motorFL.run(RELEASE);
    motorBL.run(RELEASE);
  }

  if (b != 0) {
    if (b > 0) {
      motorFR.run(FORWARD);
      motorBR.run(FORWARD);
    } else if (b < 0) {
      motorFR.run(BACKWARD);
      motorBR.run(BACKWARD);
    }
  } else {
    motorFR.run(RELEASE);
    motorBR.run(RELEASE);
  }
}
