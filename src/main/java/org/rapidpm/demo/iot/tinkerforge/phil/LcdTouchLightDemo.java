/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rapidpm.demo.iot.tinkerforge.phil;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletMultiTouch;
import com.tinkerforge.IPConnection;
import com.tinkerforge.TinkerforgeException;
import java.io.IOException;
import org.rapidpm.demo.iot.tinkerforge.devoxxpl.WaitForQ;

/**
 *
 * @author philb
 */
public class LcdTouchLightDemo {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 4223;
    private static final String MASTER_UID = "6e6sro";
    private static final String AMBIENT_UID = "mav";
    private static final String LCD_UID = "od2";
    private static final String MULTITOUCH_UID = "jU5";

    private IPConnection ipcon;

    private BrickletAmbientLight al;
    private BrickletLCD20x4 lcd;
    private BrickletMultiTouch touch;
    private int lightCallbackPeriod = 1000;

    public LcdTouchLightDemo() {

    }

    public void setup() throws IOException, TinkerforgeException {

        ipcon = new IPConnection();

        al = new BrickletAmbientLight(AMBIENT_UID, ipcon);
        lcd = new BrickletLCD20x4(LCD_UID, ipcon);
        touch = new BrickletMultiTouch(MULTITOUCH_UID, ipcon);

        ipcon.connect(HOST, PORT);

        lcd.backlightOn();
        lcd.clearDisplay();
        lcd.writeLine((short) 0, (short) 0, "Ok");

        al.setIlluminanceCallbackPeriod(lightCallbackPeriod);

        al.addIlluminanceListener((int rawValue) -> {
            try {
                writeToLcd("" + rawValue + " (" + lightCallbackPeriod + ")");
            } catch (Exception ignored) {
            }
        });

        touch.addTouchStateListener((int i) -> {
            int delta = 50;

            if (isIncreaseCommand(i)) {
                lightCallbackPeriod += delta;
                try {
                    al.setIlluminanceCallbackPeriod(lightCallbackPeriod);
                } catch (TinkerforgeException tfx) {
                }

            }

            if (isDecreaseCommand(i)) {
                lightCallbackPeriod -= delta;
                try {
                    al.setIlluminanceCallbackPeriod(lightCallbackPeriod);
                } catch (TinkerforgeException tfx) {
                }
            }

        });
    }

    private boolean isIncreaseCommand(int touchState) {
        return (touchState == 4097);
    }

    private boolean isDecreaseCommand(int touchState) {
        return (touchState == 4098);
    }

    private void writeToLcd(String msg) throws TinkerforgeException {
        lcd.clearDisplay();
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

        WaitForQ wfq = new WaitForQ();
        LcdTouchLightDemo lcdDemo = new LcdTouchLightDemo();

        try {

            wfq.addShutDownAction(() -> {
                lcdDemo.shutDown();
            });

            lcdDemo.setup();

            wfq.waitForQ();

        } catch (IOException | TinkerforgeException x) {
            System.out.println("Failed to create system: " + x.getMessage());
        }
    }

}
