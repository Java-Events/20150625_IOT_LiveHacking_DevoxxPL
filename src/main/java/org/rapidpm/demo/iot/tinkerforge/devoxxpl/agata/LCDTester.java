package org.rapidpm.demo.iot.tinkerforge.devoxxpl.agata;

import com.tinkerforge.*;
import org.rapidpm.demo.iot.tinkerforge.devoxxpl.WaitForQ;

import java.io.IOException;

/**
 * Copyright (C) 2011 Plugged-In, LLC. All rights reserved.
 * <p>
 * User: agata
 * Date: 25.06.15
 * Time: 10:50
 */
public class LCDTester {

    private static final String HOST = "localhost";
    private static final int PORT = 4223;

    private static final String LCD_UID = "ocz";
    private static final String LIGHT_UID = "mdr";
    private static final String MULTITOUCH_UID = "jSE";

    private IPConnection ip;
    private BrickletAmbientLight al;
    private BrickletLCD20x4 lcd;


    public static void main(String args[]) throws Exception {
        LCDTester tester = new LCDTester();
        tester.initialize();
        WaitForQ waiter = new WaitForQ();
        waiter.addShutDownAction(new WaitForQ.ShutDownAction() {
            @Override
            public void execute() {
                tester.shutDown();
            }
        });
        waiter.waitForQ();
    }

    private void shutDown() {
        try {
            lcd.clearDisplay();
            lcd.backlightOff();
            ip.disconnect();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private BrickletAmbientLight.IlluminanceListener illuminanceListener = new BrickletAmbientLight.IlluminanceListener() {
        public void illuminance(int illuminance) {
            try {
                double value = illuminance / 10.0;
                lcd.clearDisplay();
                lcd.writeLine((short) 0, (short) 0, value + " Lux");

            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (NotConnectedException e) {
                e.printStackTrace();
            }
        }
    };

    private void initialize() throws IOException, AlreadyConnectedException, TimeoutException, NotConnectedException {
        ip = new IPConnection();
        ip.connect(HOST, PORT);
        lcd = new BrickletLCD20x4(LCD_UID, ip);
        lcd.backlightOn();
        lcd.clearDisplay();
        al = new BrickletAmbientLight(LIGHT_UID, ip);
        al.setIlluminanceCallbackPeriod(1000);
        al.addIlluminanceListener(illuminanceListener);
    }
}
