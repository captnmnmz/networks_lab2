import java.util.LinkedList;
import java.lang.Thread;
import java.lang.InterruptedException;

public class BlockingListQueue implements URLQueue {
	
	private final LinkedList<String> queue;
	
	public BlockingListQueue() {
		queue = new LinkedList<String>();
	}

	public synchronized boolean isEmpty() {
		return queue.size() == 0;
	}

	public synchronized boolean isFull() {
		return false;
	}

	public synchronized void enqueue(String url) {
		queue.add(url);
		Thread.currentThread().notify();

	}

	public synchronized String dequeue() {
		while(queue.isEmpty()){
			try{
				Thread.currentThread().wait();
			}catch (InterruptedException e){
				System.err.println(e.getMessage());
			}

		}
		return queue.remove();
	}
	
}
