/**
 * Klasse für die Kommunikation zwischen Bieter und Auktionatoren
 */
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Communicator {
    private BlockingQueue<Message> queue;

    public Communicator() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void sendMessage(Message message) {
        this.queue.add(message);
    }

    public Message receiveMessage() {
        try {
            return this.queue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }
}