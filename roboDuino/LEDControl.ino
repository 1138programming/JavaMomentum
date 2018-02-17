#include <Adafruit_NeoPixel.h>
#include <Wire.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif


// IMPORTANT: To reduce NeoPixel burnout risk, add 1000 uF capacitor across
// pixel power leads, add 300 - 500 Ohm resistor on first pixel's data input
// and minimize distance between Arduino and first pixel.  Avoid connecting
// on a live circuit...if you must, connect GND first.


#define PIN 6
#define NUM_PIXELS 30
#define SECTION1_END 5
#define SECTION2_END 10
// Parameter 1 = number of pixels in strip
// Parameter 2 = Arduino pin number (most are valid)
// Parameter 3 = pixel type flags, add together as needed:
//   NEO_KHZ800  800 KHz bitstream (most NeoPixel products w/WS2812 LEDs)
//   NEO_KHZ400  400 KHz (classic 'v1' (not v2) FLORA pixels, WS2811 drivers)
//   NEO_GRB     Pixels are wired for GRB bitstream (most NeoPixel products)
//   NEO_RGB     Pixels are wired for RGB bitstream (v1 FLORA pixels, not v2)
//   NEO_RGBW    Pixels are wired for RGBW bitstream (NeoPixel RGBW products)
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUM_PIXELS, PIN, NEO_GRB + NEO_KHZ800);

// Blinking variables. 
//   <ordinal>Color = Main color of the LED lights
//   <ordinal>ColorBlink = Secondary color of the LED lights,
//                         used when (millis() / blinkTime) mod 2
//                         returns a nonzero value, aka every
//                         <blinkTime> milliseconds
//   blinkTime = Time between color change for each LED section.
int blinkTime = 250; // 250 ms before light change
uint32_t firstColor;
uint32_t firstColorBlink;
uint32_t secondColor;
uint32_t secondColorBlink;
uint32_t thirdColor;
uint32_t thirdColorBlink;

// The different lighting modes that can be used for
// determining how to light the LEDs. Mirrored in
// LEDSubsystem.java file in the Keystone code
typedef enum TLightModes {
  Off = 0,
  Cube,
  NumberOfModes,
} TLightModes;

// Possible responses that we can return after a command
// Either success (0), or Error (1). Error is currently
// not returned, but there is a possibility that we might
// have to use it in the future, and it's here for that
// purpose.
typedef enum Responses {
  Success = 0,
  Error
} Responses;

// Keeps track of the current mode that we're using.
TLightModes currentMode = Cube;

#define ONBOARDLED 13

void setup() {
  // This is for Trinket 5V 16MHz, you can remove these three lines if you are not using a Trinket
  #if defined (__AVR_ATtiny85__)
    if (F_CPU == 16000000) clock_prescale_set(clock_div_1);
  #endif
  // End of trinket special code

  // ADAFRUIT NEOPIXEL INITIALIZATION
  strip.begin();
  strip.show(); // Initialize all pixels to 'off'

  // Initialize the color variables
  firstColor       = strip.Color(255, 0, 0);
  firstColorBlink  = strip.Color(0, 255, 255);
  secondColor      = strip.Color(0, 255, 0);
  secondColorBlink = strip.Color(255, 0, 255);
  thirdColor       = strip.Color(0, 0, 255);
  thirdColorBlink  = strip.Color(255, 255, 0);

  // I2C INITIALIZATION - Address: 1138
  Wire.begin(0x04);             // join i2c bus with address #4
  // Todo: Stop being an idiot, Edward
  Wire.onReceive(receiveEvent); // register event

  // Serial over USB for debugging
  Serial.begin(9600);
  Serial.write("Startup.\n");

  pinMode(ONBOARDLED, OUTPUT);

}

void updateFirstChunk() {
  // This updates the first chunk of the LED strip
  int currentTime = millis();
  for (int currentPixel = 0; currentPixel < SECTION1_END; currentPixel++) {
    if ((currentTime / blinkTime) % 2) {
      strip.setPixelColor(currentPixel, firstColor);
    } else {
      strip.setPixelColor(currentPixel, firstColorBlink);
    }
  }
  // strip.show() - Used in the main loop instead
}

void updateSecondChunk() {
  // This updates the second chunk of the LED strip
  int currentTime = millis();
  for (int currentPixel = SECTION1_END; currentPixel < SECTION2_END; currentPixel++) {
    if ((currentTime / blinkTime) % 2) {
      strip.setPixelColor(currentPixel, secondColor);
    } else {
      strip.setPixelColor(currentPixel, secondColorBlink);
    }
  }
  // strip.show() - Used in the main loop instead
}

void updateThirdChunk() {
  // This updates the third chunk of the LED strip. For all functional purposes, this is an extension of the first chunk.
  int currentTime = millis();
  for (int currentPixel = SECTION2_END; currentPixel < NUM_PIXELS; currentPixel++) {
     if ((currentTime / blinkTime) % 2) {
      strip.setPixelColor(currentPixel, thirdColor);
    } else {
      strip.setPixelColor(currentPixel, thirdColorBlink);
    }
  }
  // strip.show() - Used in the main loop instead
}

/*
 * Event handler for receiving I2C data
 * 
 * We receive 1 byte at the moment, and use that to control which mode 
 * the LEDs will be put in
 */
void receiveEvent(int numBytes)
{
  int modeToUse;         // Which mode to use. 
  Serial.write("RECEIVING...\n");
  // Make sure we have 1 byte to read
  if (numBytes < 1) return;
  digitalWrite(ONBOARDLED, HIGH);
  // Read the values from the I2C connection
  modeToUse = Wire.read();
  if (modeToUse == Off || modeToUse == Cube) {
    currentMode = modeToUse;
    Wire.write(Success);
    Serial.write("Success!\n");
  } else {
    Wire.write(Error);
    Serial.write("ERROR: ");
    Serial.write(modeToUse);
    Serial.write("\n");
  }
  delay(100);
  digitalWrite(ONBOARDLED, LOW);
}


/* 
 *  The main loop - called repeatedly from main()
 *  This first updates all the LED sections on the strip,
 *  then shows it
 */
void loop() {
  updateFirstChunk();
  updateSecondChunk();
  updateThirdChunk();
  strip.show();
  if (Wire.available()) {
    Serial.write("!!");
  }
}

void serialEvent() {
  if (Serial.available()) {
    digitalWrite(ONBOARDLED, HIGH);
    Serial.read();
    //digitalWrite(ONBOARDLED, LOW);
  }
}

