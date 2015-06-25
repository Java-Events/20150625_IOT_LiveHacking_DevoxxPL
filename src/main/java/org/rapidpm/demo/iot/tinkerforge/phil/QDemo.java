/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rapidpm.demo.iot.tinkerforge.phil;

import org.rapidpm.demo.iot.tinkerforge.devoxxpl.WaitForQ;

/**
 *
 * @author philb
 */
public class QDemo {

    public static void main(String[] args) {

        WaitForQ wfq = new WaitForQ();

        wfq.addShutDownAction(() -> {
            System.out.println("q = 1");
        });

        wfq.addShutDownAction(() -> {
            System.out.println("q = 2");
        });

        wfq.addShutDownAction(() -> {
            System.out.println("q = 3");
        });

        wfq.waitForQ();
    }
}
