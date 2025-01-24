package com.example;

public class ThreadPingPong {
    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        PingThread pingThread = new PingThread(pingPong);
        PongThread pongThread = new PongThread(pingPong);
        pingThread.setName("PingThread");
        pongThread.setName("PongThread");
        // main thread starting the 2 threads
        pingThread.start();
        pongThread.start();

        try {
            // main thread waiting on the 2 threads to finish
            pingThread.join();
            pongThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

    }

    public static class PingThread extends Thread {
        // shared object for the 2 threads
        private PingPong pingPong;

        public PingThread(PingPong pingPong) {
            this.pingPong = pingPong;
        }

        @Override
        public void run() {
            // Running an infinite loop to ensure thread never finishes
            while (true) {
                pingPong.ping();
            }
        }
    }

    public static class PongThread extends Thread {
        private PingPong pingPong;

        public PongThread(PingPong pingPong) {
            this.pingPong = pingPong;
        }

        @Override
        public void run() {
            while (true) {
                pingPong.pong();
            }
        }
    }

    // Shared object class for the 2 threads
    public static class PingPong {
        public boolean isPing = true;

        /**
         * synchronized bcz two threads might be trying to simultaneously access the
         * shared pingpong object
         */
        public synchronized void ping() {
            // The loop only runs when isPing is false
            // when that happens we suspend the calling thread using wait()
            while (!isPing) {
                try {
                    // wait suspends the calling thread and releases any resources
                    // it may have locked
                    wait(); // same as Object.wait()
                } catch (InterruptedException e) {
                    System.out.println("Ping interrupted");
                }
            }
            // if isPing is True, we print "Ping"
            System.out.println("Ping from : " + Thread.currentThread().getName());
            // then set isPing=false so that we enter the loop above and suspend the current
            // thread
            isPing = false; // switch to pong's turn
            try {
                // This is for waiting 500ms before waking up the other
                // suspended thread, so that we can see Ping-Pong clearly
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("thread interrupted");
            }
            // notify wakes up the other thread that may have been suspended
            notify();

        }

        public synchronized void pong() {
            while (isPing) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Pong interrupted");
                }
            }
            System.out.println("Pong from : " + Thread.currentThread().getName());
            isPing = true;
            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
            notify();

        }
    }

}