package springframework.spring6restmvc.mappers;


import org.mapstruct.Mapper;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.models.BeerDTO;

@Mapper//All I need is this IF with this annotation and Maven will make an implementation of it
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
