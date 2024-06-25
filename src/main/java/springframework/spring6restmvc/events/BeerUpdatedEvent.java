package springframework.spring6restmvc.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import springframework.spring6restmvc.entities.Beer;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class BeerUpdatedEvent implements BeerEvent {

    private Beer beer;
    private Authentication authentication;
}
