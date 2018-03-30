import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VolatileDemo {
	private volatile boolean isRun;

	public void refresh() {
		if (isRun) {
			System.out.println("执行");
		}
	}

	public void start() {
		System.out.println("开始");
		isRun = true;
	}

	public static void main(String[] args) throws InterruptedException {
		VolatileDemo temp = new VolatileDemo();
		ExecutorService pool = Executors.newFixedThreadPool(2);
		pool.submit(new Runnable() {

			@Override
			public void run() {
				System.out.println("准备");
				while (true) {
					temp.refresh();
				}
			}
		});
		Thread.sleep(10);
		pool.submit(new Runnable() {

			@Override
			public void run() {
				temp.start();
			}
		});
	}
}
