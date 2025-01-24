package com.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        Producer producer = new Producer(queue);
        Thread producerThread = new Thread(producer, "ProducerThread");
        producerThread.start();

        // create and start two consumer threads
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
        Thread consumerThread1 = new Thread(consumer1, "ConsumerThread-1");
        Thread consumerThread2 = new Thread(consumer2, "ConsumerThread-2");

        consumerThread1.start();
        consumerThread2.start();

    }

    public static class Producer implements Runnable {
        private final BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 20; i++) {
                    String task = "Task-" + i;
                    queue.put(task);
                    System.out.println(Thread.currentThread().getName() + " produced: " + task);
                    Thread.sleep(200);
                }
                // add poison pill to signal consumers to stop
                queue.put("POISON_PILL");
                queue.put("POISON_PILL"); // add second poison pill for second consumer
                System.out.println(Thread.currentThread().getName() + " added POISON_PILL to the queue");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted");
            }
        }
    }

    public static class Consumer implements Runnable {
        private final BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String task = queue.take();
                    if (task.equals("POISON_PILL")) {
                        System.out.println(Thread.currentThread().getName() + " received POISON_PILL exiting...");
                        break;
                    }
                    System.out.println(Thread.currentThread().getName() + " consumed: " + task);
                    Thread.sleep(500); // simulate time taken to process a task
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted");
            }
        }
    }
}
