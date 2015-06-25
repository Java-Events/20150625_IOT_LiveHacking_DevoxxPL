/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rapidpm.demo.iot.tinkerforge.phil;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.TinkerforgeException;
import java.io.IOException;

/**
 *
 * @author philb
 */
public class LcdDemo {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 4223;
    private static final String MASTER_UID = "6e6sro";
    private static final String AMBIENT_UID = "mav";
    private static final String LCD_UID = "od2";

    private final BrickletAmbientLight al;
    private final BrickletLCD20x4 lcd;
    private final IPConnection ipcon;

    public LcdDemo() throws IOException, TinkerforgeException {
        ipcon = new IPConnection();

        al = new BrickletAmbientLight(AMBIENT_UID, ipcon);
        lcd = new BrickletLCD20x4(LCD_UID, ipcon);

        ipcon.connect(HOST, PORT);
    }

    public void run() throws TinkerforgeException {

        lcd.backlightOn();
        lcd.clearDisplay();
        lcd.writeLine((short) 0, (short) 0, "Ok");

        al.setIlluminanceCallbackPeriod(1000);

        al.addIlluminanceListener((int rawValue) -> {
            System.out.println("Got ambient light raw value: " + rawValue);

            try {
                writeToLcd("" + rawValue);
            } catch (Exception ignored) {
            }
        });
    }

    private void writeToLcd(String msg) throws TinkerforgeException {
        lcd.writeLine((short) 0, (short) 0, msg);
    }

    private void shutDown() {

        try {
            lcd.clearDisplay();
        } catch (TinkerforgeException ex) {
        }

        try {
            lcd.backlightOff();
        } catch (TinkerforgeException ex) {
        }

        try {
            ipcon.disconnect();
        } catch (TinkerforgeException ex) {
        }
    }

    public static void main(String[] args) {

        LcdDemo lcdDemo;

        try {
            lcdDemo = new LcdDemo();
        } catch (IOException | TinkerforgeException x) {
            System.out.println("Failed to create system: " + x.getMessage());
            return;
        }

        try {
            lcdDemo.run();

            System.out.println("Press key to exit");
            System.in.read();

        } catch (IOException | TinkerforgeException tfx) {
            System.out.println("Error running demo: " + tfx.getMessage());
        } finally {
            lcdDemo.shutDown();
        }
    }

}
