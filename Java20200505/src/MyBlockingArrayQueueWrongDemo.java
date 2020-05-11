import java.util.Scanner;
/*
演示多生产者、多消费者模型下的BUG
 */
public class MyBlockingArrayQueueWrongDemo {
    //定义一个队列对象-生产者线程是Produce，消费者线程是main线程
    static MyBlockingArrayQueue queue = new MyBlockingArrayQueue();
    //定义一个生产者线程类
    static class Producer extends Thread{
        @Override
        public void run() {
            try {
                int i = 0;
                while (true){
                    queue.put(i);
                    i++;
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
      Producer producer1 = new Producer();
      producer1.start();
      Producer producer2 = new Producer();
      producer2.start();
      Producer producer3 = new Producer();
      producer3.start();

        Thread.sleep(2*1000);

        Scanner scanner = new Scanner(System.in);
        while (true){
      queue.take();
        }
    }
}

