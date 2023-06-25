/* TimeoutCaller class
 * Author and owner: Esteban Sánchez Llobregat (@FosanzDev)
 * License: Apache-2.0
 * 
 * Class used to run code after a certain amount of time.
 * Objects should be closed once they are not needed anymore. (Use cancel() method to do so)
 * (IMPORTANT) If the object is not closed, it will run forever.
 * 
 * On the constructor, if onlyOnce is set to true, the code will only run once and then it will stop.
 * If onlyOnce is set to false, the code will run forever.
 */

package com.fosanzdev.Utils;

public class TimeoutCaller extends Thread{

    private long timeout;
    private Runnable runnable;
    private boolean running;

    private boolean onlyOnce;

    public TimeoutCaller(long timeout, Runnable runnable, boolean onlyOnce){
        super();
        this.timeout = timeout;
        this.runnable = runnable;
        this.onlyOnce = onlyOnce; 
        running = true;
    }

    public TimeoutCaller(long timeout, Runnable runnable){
        this(timeout, runnable, false);
    }

    @Override
    public void run(){
        long start = System.currentTimeMillis();
        while (running){
            if (System.currentTimeMillis() - start >= timeout && running) {
                runnable.run();
                if (onlyOnce)
                    cancel();
                start = System.currentTimeMillis();
            } else {
                // Do nothing (wait for timeout to end)
            }
        }
    }

    public void cancel(){
        running = false;
    }
}
