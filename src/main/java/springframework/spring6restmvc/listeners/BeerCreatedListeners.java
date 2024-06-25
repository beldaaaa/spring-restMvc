package springframework.spring6restmvc.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.events.BeerCreatedEvent;

@Component
public class BeerCreatedListeners {
    @Async
    @EventListener
    public void listen(BeerCreatedEvent event) {
        System.out.println(event.getBeer().getId() + " is created");
        System.out.println("Thread name: " + Thread.currentThread().getName() + " with ID: " + Thread.currentThread().getId());
    }
}
