import java.util.HashSet;
import java.util.NoSuchElementException;
import java.lang.Thread;

class URLThread implements Runnable{
	/**
	 * A thread that lives and dies around one url download
	 */
	String requestedURL;
	String proxyHost; 
	int proxyPort;
	
	public URLThread(String requestedURL, String proxyHost,int proxyPort) {
		this.requestedURL=  requestedURL;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	public void run() {
		Xurl.query(requestedURL, proxyHost, proxyPort);
	}
}

class URL_pool implements Runnable{
	/**
	 * A thread that is created for a pool, and dies once there is no more work
	 */
	URLQueue queue;
	String proxyHost; 
	int proxyPort;	
	
	public URL_pool(URLQueue queue,String proxyHost,int proxyPort) {
		this.queue=queue;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}
	

	
	public void run() {
			while(true){
				String requestedURL=queue.dequeue();
				if (requestedURL==""){
					break;
				}
				Xurl.query(requestedURL, proxyHost, proxyPort);
			}

	}
}

public class Wget {	
	
	public static void doMultiThreaded(String requestedURL, String proxyHost,
			int proxyPort) {
		final URLQueue queue = new SynchronizedListQueue();
		final HashSet<String> seen = new HashSet<String>();
		// Synchronized
		URLprocessing.handler = new URLprocessing.URLhandler() {
			// this method is called for each matched url
			public void takeUrl(String url) {
				// Add a condition to wait
				if (!seen.contains(url)) {
					queue.enqueue(url);
					seen.add(url);
				}
			}
		};
		// to start, we push the initial url into the queue
		URLprocessing.handler.takeUrl(requestedURL);
		int count = Thread.activeCount();
		while (Thread.activeCount()>count || !queue.isEmpty()) {
			if (!queue.isEmpty()){
				try {
					String url = queue.dequeue();
					URLThread url_thread = new URLThread(url, proxyHost, proxyPort);
					Thread _thread = new Thread(url_thread);
					_thread.start();
				}catch(NoSuchElementException e) {
					System.err.println(e.getMessage());
				}
			}else{
			}


		}
		
	}
	
	public static void doThreadedPool(String requestedURL, String proxyHost,
			int proxyPort){
		
		final URLQueue queue = new BlockingListQueue();
		final HashSet<String> seen = new HashSet<String>();
		final int number_threads = 50;
		
		
		Thread[] thread_list = new Thread[number_threads];
		// Synchronized
		URLprocessing.handler = new URLprocessing.URLhandler() {
			// this method is called for each matched url
			public void takeUrl(String url) {
				// Add a condition to wait
				if (!seen.contains(url)) {
					queue.enqueue(url);
					seen.add(url);
				}
			}
		};
		// to start, we push the initial url into the queue
		URLprocessing.handler.takeUrl(requestedURL);
		
		ThreadGroup group = new ThreadGroup("pool");
		
		while (group.activeCount()!=number_threads) {
			URL_pool _pool = new URL_pool(queue,proxyHost,proxyPort);
			Thread _thread = new Thread(group,_pool);
			_thread.start();


		}
		
		while(true){
			int count = group.enumerate(thread_list);
			int inc=0;
			for (Thread t : thread_list){
				if (t.getState()==Thread.State.WAITING){
					inc++;
				}
			}
			if (inc==count){
				for (int i=0; i<number_threads;i++){
					queue.enqueue("");
				}
			}
			break;
		}
	}


	public static void doIterative(String requestedURL, String proxyHost,
			int proxyPort) {
		final URLQueue queue = new ListQueue();
		final HashSet<String> seen = new HashSet<String>();
		URLprocessing.handler = new URLprocessing.URLhandler() {
			// this method is called for each matched url
			public void takeUrl(String url) {
				// to be completed
				if (!seen.contains(url)) {
					queue.enqueue(url);
					seen.add(url);
				}
			}
		};
		// to start, we push the initial url into the queue
		URLprocessing.handler.takeUrl(requestedURL);
		while (!queue.isEmpty()) {
			String url = queue.dequeue();
			Xurl.query(url, proxyHost, proxyPort); // or equivalent yours
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java Wget url [proxyHost proxyPort]");
			System.exit(-1);
		}
		String proxyHost = null;
		if (args.length > 1)
			proxyHost = args[1];
		int proxyPort = -1;
		if (args.length > 2)
			proxyPort = Integer.parseInt(args[2]);
		doThreadedPool(args[0], proxyHost, proxyPort);
	}

}
