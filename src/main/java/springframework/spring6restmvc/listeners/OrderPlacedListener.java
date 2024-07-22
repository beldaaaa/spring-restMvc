package springframework.spring6restmvc.listeners;

import org.springframework.stereotype.Component;
import springframework.springrestmvcapi.model.events.OrderPlacedEvent;

@Component
public class OrderPlacedListener {

    public void listen(OrderPlacedEvent orderPlacedEvent) {
        System.out.println(orderPlacedEvent);
    }
}
