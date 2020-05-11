import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class CustomThreadPool {
    static class Worker extends Thread{
        private BlockingQueue<Runnable> workQueue;

        Worker(BlockingQueue<Runnable> workQueue){          //构造方法
            this.workQueue = workQueue;
        }

        @Override
        public void run() {
            //员工要做的事情
            //不停地从队列中取任务

            //while (true){
            while (!Thread.interrupted()){
                Runnable task = null;
                try {
                    //从队列中取任务,当队列为空时，线程就会阻塞，否则就一直工作
                    task = workQueue.take();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    //这里表示收到了interrupt通知
                    break;      //退出
                }
                //完成任务
                task.run();
            }
        }
    }

    // 1.传递任务用的阻塞队列
    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();

    // 2.基础所有的线程对象
    //相当于公司里管理员工用的员工表
    List<Worker> workerList = new ArrayList<>();

    CustomThreadPool(int nThread){
        // 1.创建阻塞队列，done
        //创建所有的工作线程
        for (int i = 0; i < nThread; i++) {
            Worker worker = new Worker(workQueue);
            worker.start();
            workerList.add(worker);
        }
    }

    //前台所做的事就是把接到的任务放到队列中即可
    //工作线程会在合适的时候过来取任务的
    void execute(Runnable task) throws InterruptedException {
        workQueue.put(task);
    }
    void shutDown(){
        //让所有的线程都停止工作
        //可以使用interrupt建议一个线程停下来
        //实际上Worker什么时候停，你处理不了
        for (Worker worker :  workerList) {
            worker.interrupt();
        }
    }
}
