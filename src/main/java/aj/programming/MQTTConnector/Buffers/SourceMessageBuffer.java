package aj.programming.MQTTConnector.Buffers;

import aj.programming.MQTTConnector.DTO.MessageDTO;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SourceMessageBuffer {
    private final BlockingQueue<MessageDTO> blockingQueue;
    private int i = 0;

    public SourceMessageBuffer() {
        this.blockingQueue = new LinkedBlockingQueue<>();
    }

    public void addMessage(String message) {
        this.blockingQueue.add(new MessageDTO(message, String.valueOf(UUID.randomUUID()), this.i, System.currentTimeMillis()));
        this.i += 1;
    }

    public boolean isEmpty() {
        return this.blockingQueue.isEmpty();
    }

    public MessageDTO poll() throws InterruptedException {
        return this.blockingQueue.take();
    }
}
