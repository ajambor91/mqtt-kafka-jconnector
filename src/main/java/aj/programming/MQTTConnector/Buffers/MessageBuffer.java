package aj.programming.MQTTConnector.Buffers;

import aj.programming.MQTTConnector.DTO.MessageDTO;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageBuffer {
    private final BlockingQueue<MessageDTO> blockingQueue;
    private int i = 0;

    public MessageBuffer() {
        this.blockingQueue = new LinkedBlockingQueue<>();
    }

    public void addMessage(MessageDTO message) {
        this.blockingQueue.add(message);
        this.i = message.getMessageNumber();
    }

    public void addMessage(String message, String clientId) {
        this.blockingQueue.add(new MessageDTO(message, clientId, String.valueOf(UUID.randomUUID()), this.i, System.currentTimeMillis()));
        this.i += 1;
    }

    public boolean isEmpty() {
        return this.blockingQueue.isEmpty();
    }

    public MessageDTO poll() throws InterruptedException {
        return this.blockingQueue.take();
    }
}
