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
public class TouchDemo {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 4223;
    private static final String LCD_UID = "od2";
    private static final String MULTITOUCH_UID = "jU5";

    private IPConnection ipcon;

    private BrickletLCD20x4 lcd;
    private BrickletMultiTouch touch;

    public TouchDemo() {

    }

    public void setup() throws IOException, TinkerforgeException {

        ipcon = new IPConnection();

        lcd = new BrickletLCD20x4(LCD_UID, ipcon);
        touch = new BrickletMultiTouch(MULTITOUCH_UID, ipcon);

        ipcon.connect(HOST, PORT);

        lcd.backlightOn();
        lcd.clearDisplay();
        lcd.writeLine((short) 0, (short) 0, "Ok");

        touch.addTouchStateListener((int touchState) -> {
            String str = "";

            if ((touchState & (1 << 12)) == (1 << 12)) {
                str += "In proximity, ";
            }

            if ((touchState & 0xfff) == 0) {
                str += "No electrodes touched" + System.getProperty("line.separator");
            } else {
                str += "Electrodes ";
                for (int i = 0; i < 12; i++) {
                    if ((touchState & (1 << i)) == (1 << i)) {
                        str += i + " ";
                    }
                }
                str += "touched" + System.getProperty("line.separator");
            }

            System.out.println(str);
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

        WaitForQ wfq = new WaitForQ();
        TouchDemo touchDemo = new TouchDemo();

        try {

            wfq.addShutDownAction(() -> {
                touchDemo.shutDown();
            });

            touchDemo.setup();

            wfq.waitForQ();

        } catch (IOException | TinkerforgeException x) {
            System.out.println("Failed to create system: " + x.getMessage());
        }
    }

}
