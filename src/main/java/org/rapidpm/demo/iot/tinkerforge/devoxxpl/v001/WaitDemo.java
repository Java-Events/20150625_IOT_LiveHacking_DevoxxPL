package org.rapidpm.demo.iot.tinkerforge.devoxxpl.v001;

import org.rapidpm.demo.iot.tinkerforge.devoxxpl.WaitForQ;

/**
 * Created by svenruppert on 25.06.15.
 */
public class WaitDemo {

  public static void main(String[] args) {

    WaitForQ q = new WaitForQ();


    q.addShutDownAction(() -> System.out.println("q = 1"));
    q.addShutDownAction(() -> System.out.println("q = 2"));
    q.addShutDownAction(() -> System.out.println("q = 3"));

    q.waitForQ();



  }


}
