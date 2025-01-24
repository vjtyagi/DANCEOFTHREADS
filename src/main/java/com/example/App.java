package com.example;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();
        // Main thread sleeps for 2 seconds then interrupts MyThread
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted");
        }
        System.out.println("Main thread interrupting MyThread");
        thread.interrupt();
    }

    private static class MyThread extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Mythread is running: " + i);
                    // check if thread has been interrupted
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Mythread was interrupted");
                        return;
                    }
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println("MyThread was interrupted while sleeping");
                Thread.currentThread().interrupt(); // restore interrupted status
            }
        }
    }
}
