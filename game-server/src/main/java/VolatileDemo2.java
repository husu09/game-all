import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VolatileDemo2 {

	int num = 0;

	private List<Integer> list;

	public void doSomthing() {
		synchronized (this) {
			if (list != null) {
				System.out.println("doSomthing()");
				System.out.println(list);
			}
		}
	}

	public void refresh() {
		synchronized (this) {
			List<Integer> list = new ArrayList<>();
			for (int i = 1; i <= 5; i++) {
				num += 1;
				list.add(num);
			}
			this.list = list;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		VolatileDemo2 temp = new VolatileDemo2();
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
		pool.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				temp.doSomthing();
			}
		}, 0, 1, TimeUnit.SECONDS);
		pool.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				temp.refresh();
			}
		}, 0, 5, TimeUnit.SECONDS);
	}
}
