package com.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class EventLoop {
    public static void main(String[] args) {
        BlockingQueue<Runnable> taskQueue1 = new LinkedBlockingQueue<>();
        BlockingQueue<Runnable> taskQueue2 = new LinkedBlockingQueue<>();

        // Create the Event-loop thread
        Loop loop = new Loop(taskQueue1, taskQueue2);
        Thread loopThread = new Thread(loop, "EventLoopThread");
        loopThread.start();

        // populate the task queues
        populateTaskQueues(taskQueue1, taskQueue2);

        // Keep main thread alive
        try {
            loopThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main Thread interrupted");
        }

    }

    public static class Loop implements Runnable {
        private final BlockingQueue<Runnable> taskQueue1;
        private final BlockingQueue<Runnable> taskQueue2;

        public Loop(BlockingQueue<Runnable> taskQueue1, BlockingQueue<Runnable> taskQueue2) {
            this.taskQueue1 = taskQueue1;
            this.taskQueue2 = taskQueue2;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Runnable task1 = taskQueue1.poll();
                    if (task1 != null) {
                        System.out.println(Thread.currentThread().getName() + " executing task from queue 1");
                        task1.run();
                    }
                    // Check taskQueue2
                    Runnable task2 = taskQueue2.poll();
                    if (task2 != null) {
                        System.out.println(Thread.currentThread().getName() + " executing task from queue 2");
                        task2.run();
                    }
                    Thread.sleep(100); // sleep for short time to avoid busy-waiting
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " Interrupted");
                    break; // Exit the loop if interrupted
                }
            }
            System.out.println(Thread.currentThread().getName() + " exiting ");
        }
    }

    private static void populateTaskQueues(BlockingQueue<Runnable> taskQueue1, BlockingQueue<Runnable> taskQueue2) {
        // Add tasks to queue 1
        taskQueue1.add(() -> System.out.println("Task 1 from queue 1"));
        taskQueue1.add(() -> System.out.println("Task 2 from queue 1"));
        taskQueue1.add(() -> System.out.println("Task 3 from queue 1"));

        taskQueue2.add(() -> System.out.println("Task 1 from queue 2"));
        taskQueue2.add(() -> System.out.println("Task 2 from queue 2"));
        taskQueue2.add(() -> System.out.println("Task 3 from queue 2"));
    }
}
