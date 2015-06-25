/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie.philb.tinkersample1;

import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.IPConnection;
import com.tinkerforge.TinkerforgeException;
import java.io.IOException;

/**
 *
 * @author philb
 */
public class Main {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 4223;
    private static final String UID = "6e6sro";

    public static void main(String[] args) throws IOException, TinkerforgeException {

        IPConnection ipcon = new IPConnection();
        BrickletAmbientLight al = new BrickletAmbientLight(UID, ipcon);

        try {
            ipcon.connect(HOST, PORT);
        } catch (IOException | TinkerforgeException ex) {
            System.err.println("Failed to connect " + ex.getMessage());
            return;
        }

        try {
            al.setIlluminanceCallbackPeriod(1000);

            al.addIlluminanceListener(new BrickletAmbientLight.IlluminanceListener() {
                @Override
                public void illuminance(int rawValue) {
                    System.out.println("Got ambient light raw value: " + rawValue);
                }
            });

        } catch (TinkerforgeException ex) {
            System.err.println("Tinkerforge error : " + ex.getMessage());
        }

        System.out.println("Press key to exit");
        System.in.read();
        ipcon.disconnect();
    }
}
