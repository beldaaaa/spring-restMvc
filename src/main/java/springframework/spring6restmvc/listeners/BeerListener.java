package springframework.spring6restmvc.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.events.*;
import springframework.spring6restmvc.mappers.BeerMapper;
import springframework.spring6restmvc.repositories.BeerAuditRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerListener {

    private final BeerMapper beerMapper;
    private final BeerAuditRepository beerAuditRepository;

    @Async
    @EventListener
    public void listen(BeerEvent event) {

        var beerAudit = beerMapper.beerToBeerAudit(event.getBeer());
        String eventType = switch (event) {
            case BeerCreatedEvent beerCreatedEvent -> "BEER_CREATED";
            case BeerUpdatedEvent beerUpdatedEvent -> "BEER_UPDATED";
            case BeerPatchedEvent beerPatchedEvent -> "BEER_PATCHED";
            case BeerDeletedEvent beerDeletedEvent -> "BEER_DELETED";
            default -> "NOT_DEFINED";
        };

        beerAudit.setAuditEventType(eventType);

        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        var savedBeerAudit = beerAuditRepository.save(beerAudit);
        log.debug("beerAudit saved: {} {}", eventType, savedBeerAudit.getAuditId());
    }
}
