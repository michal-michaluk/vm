package micro.points;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@AllArgsConstructor
public class EventListeners {

    private final LocationUpdateClient client;

    @TransactionalEventListener
    public void listen(LocationUpdated event) {
        client.sendLocationUpdate(event.getId(), event.getLocation());
    }
}
