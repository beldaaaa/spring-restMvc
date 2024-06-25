package springframework.spring6restmvc.events;

import org.springframework.security.core.Authentication;
import springframework.spring6restmvc.entities.Beer;

public interface BeerEvent {

    Beer getBeer();

    Authentication getAuthentication();
}
