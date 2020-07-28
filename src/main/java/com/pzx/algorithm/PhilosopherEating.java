package com.pzx.algorithm;

import com.sun.scenario.effect.impl.prism.PrImage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 哲学家进餐问题
 * 有限资源的竞争问题
 */
public class PhilosopherEating {

    private static final int N = 5;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Philosopher(0));
        executorService.submit(new Philosopher(1));
        executorService.submit(new Philosopher(2));
        executorService.submit(new Philosopher(3));
        executorService.submit(new Philosopher(4));

    }

    private static class Philosopher implements Runnable{

        private final static int THINKING = 0;
        private final static int HUNGRY = 1;
        private final static int EATING = 2;
        private final static Lock lock;
        private static final int[] state;
        private static final Semaphore[] semaphores;

        static {
            lock = new ReentrantLock();
            state = new int[N];
            semaphores = new Semaphore[N];
            for (int i = 0; i < N; i++){
                semaphores[i] = new Semaphore(1);
                semaphores[i].acquireUninterruptibly();
            }
        }

        private int index;

        public Philosopher(int index) {
            this.index = index;
        }

        /**
         * 哲学家拿起旁边的两个叉子
         */
        public void takeForks() throws InterruptedException{
            lock.lock(); //同一时刻只能有一个哲学家拿起叉子
            state[index] = HUNGRY; //表示当前正在试图拿起叉子
            test(index); //尝试拿起两个叉子
            lock.unlock(); //当前哲学家完成拿叉子操作（成功或者失败未知）
            semaphores[index].acquire();//当拿起叉子成功，则此处不会堵塞，方法直接返回；否则此处信号量导致堵塞，表示还未成功拿起叉子。只有当前左右哲学家放下叉子之后，其才可以继续向下运行。
            System.out.println(index + "号哲学家拿起叉子");
        }

        /**
         * 哲学家放下旁边的两个叉子
         */
        public void putForks(){

            lock.lock(); //同一时刻只能有一个哲学家放下叉子
            state[index] = THINKING; //表示哲学家eating之后进行thinking状态
            System.out.println(index + "号哲学家放下叉子");
            //下面判断左右哲学家是因为，他们可能因为当前哲学家拿起了左右叉子导致其处于阻塞状态。
            test(left(index)); // 判断左边的哲学家是否处于尝试拿叉子状态，是，则该哲学家之前一定拿起失败处于阻塞状态，判断其是否可以拿起叉子进行处理；否，则说明哲学家处于thinking状态。
            test(right(index)); //同样判断右边的哲学家
            lock.unlock();
        }


        private void test(int index){
            //如果当前哲学家正在试图拿起叉子，而且其左右哲学家都没有成功拿起叉子
            if(state[index] == HUNGRY && state[left(index)] != EATING && state[right(index)] != EATING){
                state[index] = EATING; //当前哲学家成功拿起两个叉子，进入eating状态
                semaphores[index].release(); //将当前哲学家的信号量赋值为1
            }
        }

        private int left(int index){
            return (index + N - 1) % N;
        }

        private int right(int index){
            return (index + 1) % N;
        }

        public void think() throws InterruptedException{
            System.out.println(index + "号哲学家正在思考");
            Thread.sleep(ThreadLocalRandom.current().nextInt(4));

        }

        public void eat() throws InterruptedException{
            System.out.println(index + "号哲学家正在吃饭");
            Thread.sleep(ThreadLocalRandom.current().nextInt(4));

        }
        @Override
        public void run() {
            try {
                while (true){
                    think();
                    takeForks();
                    eat();
                    putForks();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

}
