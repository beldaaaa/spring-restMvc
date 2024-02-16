package springframework.spring6restmvc.mappers;

import org.mapstruct.Mapper;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.model.BeerDTO;

@Mapper//automatically maps between 2 Java Beans
//all I need is to create this IF and mapstruct generates the concrete implementation
public interface BeerMapper {
    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
