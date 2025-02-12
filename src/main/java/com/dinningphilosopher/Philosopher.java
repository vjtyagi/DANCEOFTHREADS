package com.dinningphilosopher;

import java.util.Random;

public class Philosopher implements Runnable {
    private int id;
    private Chopstick leftChopstick;
    private Chopstick rightChopstick;
    private int eatingCounter;
    private Random random;
    private volatile boolean full;

    public Philosopher(int id, Chopstick leftChopstick, Chopstick rightChopstick) {
        this.id = id;
        this.leftChopstick = leftChopstick;
        this.rightChopstick = rightChopstick;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            while (!full) {
                think();
                if (leftChopstick.pickup(this, State.LEFT)) {
                    if (rightChopstick.pickup(this, State.RIGHT)) {
                        eat();
                        rightChopstick.putDown(this, State.RIGHT);
                    }
                    leftChopstick.putDown(this, State.LEFT);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eat() throws InterruptedException {
        System.out.println(this + " is eating");
        eatingCounter++;
        Thread.sleep(random.nextInt(1000));
    }

    public void think() throws InterruptedException {
        System.out.println(this + " is thinking ");
        Thread.sleep(random.nextInt(1000));
    }

    public int getEatingCounter() {
        return eatingCounter;
    }

    public void setEatingCounter(int eatingCounter) {
        this.eatingCounter = eatingCounter;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    @Override
    public String toString() {
        return "Philosopher [id=" + id + "]";
    }

}
