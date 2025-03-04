package aj.programming.MQTTConnector.Buffers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceMessageBuffer {
    private final BlockingQueue<String> blockingQueue;

    public SourceMessageBuffer() {
        this.blockingQueue = new LinkedBlockingQueue<>();
    }

    public void addMessage(String message) {
        this.blockingQueue.add(message);
    }

    public boolean isEmpty() {
        return this.blockingQueue.isEmpty();
    }

    public String poll() throws InterruptedException {
        return this.blockingQueue.take();
    }
}
