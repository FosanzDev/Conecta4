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
            if (System.currentTimeMillis() - start >= timeout) {
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
