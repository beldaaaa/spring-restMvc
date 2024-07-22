package springframework.spring6restmvc.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.entities.BeerAudit;
import springframework.springrestmvcapi.BeerDTO;

@Mapper//All I need is this IF with this annotation and Maven will make an implementation of it
public interface BeerMapper {

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);

    @Mapping(target = "createdDateAudit", ignore = true)
    @Mapping(target = "auditId", ignore = true)
    @Mapping(target = "principalName", ignore = true)
    @Mapping(target = "auditEventType", ignore = true)
    BeerAudit beerToBeerAudit(Beer beer);

}
