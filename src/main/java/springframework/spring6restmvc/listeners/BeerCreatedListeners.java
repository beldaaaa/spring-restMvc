package springframework.spring6restmvc.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.events.BeerCreatedEvent;

@Component
public class BeerCreatedListeners {

    @EventListener
    public void listen(BeerCreatedEvent event) {
        System.out.println(event.getBeer().getId() + " is created");
    }
}
