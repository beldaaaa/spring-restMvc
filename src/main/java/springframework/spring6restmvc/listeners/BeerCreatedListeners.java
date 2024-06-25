package springframework.spring6restmvc.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.events.BeerCreatedEvent;
import springframework.spring6restmvc.mappers.BeerMapper;
import springframework.spring6restmvc.repositories.BeerAuditRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerCreatedListeners {

    private final BeerMapper beerMapper;
    private final BeerAuditRepository beerAuditRepository;

    @Async
    @EventListener
    public void listen(BeerCreatedEvent event) {

        var beerAudit = beerMapper.beerToBeerAudit(event.getBeer());
        beerAudit.setAuditEventType("beer created");

        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        var savedBeerAudit = beerAuditRepository.save(beerAudit);
        log.debug("beerAudit saved: " + savedBeerAudit.getAuditId());
    }
}
