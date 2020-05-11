import java.util.Scanner;

/*
 * 1.是不是线程安全的？是否有共享，是否修改？
 * 2.怎么改成线程安全的版本?通过加锁解决
 *3.生产者在队列满时等待-消费者在队列空是等待
 * 4.生产者需要唤醒可能等消费者；消费者需要唤醒可能在等的生产者
 * */
public class MyBlockingArrayQueue {
    int[] array = new int[10];  //下标处的数据可能生产者和消费者修改同一处的情况
    int front = 0;         //只有消费者修改front
    int rear = 0;           //只有生产者修改rear
    int size = 0;           //size是生产者消费者都会修改的

    //生产者才会调用put
    synchronized void put(int value) throws InterruptedException {
        //考虑满的情况
        if (size == array.length){
            //队列已满
            //throw new RuntimeException("队列已满");
            wait();     //其实this.wait()；问我现在已经把this指向的对象锁住了吗？
                            //synchronized修饰普通方法，就等于锁得是this指向的对象
        }
        array[rear] = value;
        rear = (rear+1)%array.length;
        size++;                     //我们需要保证的是size++ 的原子性，所以volatile无法解决（用其修饰size）
        notify();
    }

    //调用take的一定是消费者
    synchronized int take() throws InterruptedException {
        //考虑空的情况
        if (size == 0){
            //throw new RuntimeException("队列已空");
            wait();
        }

        int value = array[front];
        front = (front + 1)%array.length;
        size--;
        System.out.println(size);
        notify();
        return  value;
    }


    //定义一个队列对象-生产者线程是Produce，消费者线程是main线程
    static MyBlockingArrayQueue queue = new MyBlockingArrayQueue();
//定义一个生产者线程类
    static class Producer extends Thread{
        @Override
        public void run() {
            try {
                for (int i = 0; i < 100; i++) {
                    System.out.println("准备放入"+i);
                    queue.put(i);
                    System.out.println("放入成功");
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        producer.start();

        Thread.sleep(2*1000);

        Scanner scanner = new Scanner(System.in);
        while (true){
            scanner.nextLine();
            System.out.println(queue.take());
        }
    }
}
