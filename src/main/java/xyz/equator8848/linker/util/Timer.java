package xyz.equator8848.linker.util;

/**
 * 计时器
 *
 * @Author: Equator
 * @Date: 2022/7/10 12:27
 **/

public class Timer {
    private long start;

    public Timer() {
        this.start = System.currentTimeMillis();
    }

    public long cost() {
        return System.currentTimeMillis() - start;
    }

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        Thread.sleep(1000);
        System.out.println(timer.cost());
    }
}
