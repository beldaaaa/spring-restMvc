package springframework.spring6restmvc.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import springframework.spring6restmvc.entities.Beer;

//do not use @Data here, Beer entity has references to other entities => it can cause endless circular loop
//for the two strings, equals and hashcode
@Builder
@AllArgsConstructor
@Setter
@Getter
public class BeerCreatedEvent {

    private Beer beer;
    private Authentication authentication;
}
