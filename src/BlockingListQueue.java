import java.util.LinkedList;
import java.lang.Thread;
import java.io.IOException;
import java.lang.InterruptedException;

public class BlockingListQueue implements URLQueue {
	/**
	 * @author - Jules YATES
	 * @author - Bastien CHEVALLIER
	 */
	
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
		notify();
		
	}

	public synchronized String dequeue() {
		while(queue.isEmpty()){
			try{
				wait();
			}catch (InterruptedException e){
				System.err.println(e.getMessage());
			}

		}
		return queue.remove();
	}
	
}
