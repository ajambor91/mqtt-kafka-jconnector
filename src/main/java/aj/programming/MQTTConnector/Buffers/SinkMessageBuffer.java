package aj.programming.MQTTConnector.Buffers;

import aj.programming.MQTTConnector.DTO.MessageDTO;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SinkMessageBuffer {
    private final BlockingQueue<MessageDTO> blockingQueue;
    private int i = 0;

    public SinkMessageBuffer() {
        this.blockingQueue = new LinkedBlockingQueue<>();
    }

    public void addMessage(MessageDTO message) {
        this.blockingQueue.add(message);
        this.i = message.getMessageNumber();
    }

    public boolean isEmpty() {
        return this.blockingQueue.isEmpty();
    }

    public MessageDTO poll() throws InterruptedException {
        return this.blockingQueue.take();
    }
}
