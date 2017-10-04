import java.util.LinkedList;
import java.util.NoSuchElementException;

public class SynchronizedListQueue implements URLQueue {
	
	private final LinkedList<String> queue;

	public SynchronizedListQueue() {
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
	}

	public synchronized String dequeue() {
		if (this.isEmpty()) {
			throw new NoSuchElementException("The queue is already empty");
		}
		return queue.remove();
	}

}
