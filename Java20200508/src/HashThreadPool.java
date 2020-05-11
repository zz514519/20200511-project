import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HashThreadPool {
    static class 送快递任务 implements Runnable{
    String 快递;

    public 送快递任务(String 快递) {
        this.快递 = 快递;
    }

    @Override
    public void run() {
        System.out.println("开始送快递："+快递);
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
    public static void main(String[] args) {
        //开了一家公司，有十个员工
        ExecutorService 公司 = Executors.newFixedThreadPool(10);
        //主线程就是前台
        Scanner scanner = new Scanner(System.in);
        while (true){
            String 快递 = scanner.nextLine();
            送快递任务 task = new 送快递任务(快递);
            公司.execute(task);
        }
    }
}
