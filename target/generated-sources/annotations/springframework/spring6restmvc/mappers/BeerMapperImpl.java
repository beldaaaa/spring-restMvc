package springframework.spring6restmvc.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.model.BeerDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-08T17:23:43+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Azul Systems, Inc.)"
)
@Component
public class BeerMapperImpl implements BeerMapper {

    @Override
    public Beer beerDtoToBeer(BeerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Beer.BeerBuilder beer = Beer.builder();

        beer.id( dto.getId() );
        beer.version( dto.getVersion() );
        beer.beerName( dto.getBeerName() );
        beer.beerStyle( dto.getBeerStyle() );
        beer.upc( dto.getUpc() );
        beer.quantityOnHand( dto.getQuantityOnHand() );
        beer.price( dto.getPrice() );
        beer.createdData( dto.getCreatedData() );
        beer.updateData( dto.getUpdateData() );

        return beer.build();
    }

    @Override
    public BeerDTO beerToBeerDto(Beer beer) {
        if ( beer == null ) {
            return null;
        }

        BeerDTO.BeerDTOBuilder beerDTO = BeerDTO.builder();

        beerDTO.id( beer.getId() );
        beerDTO.version( beer.getVersion() );
        beerDTO.beerName( beer.getBeerName() );
        beerDTO.beerStyle( beer.getBeerStyle() );
        beerDTO.upc( beer.getUpc() );
        beerDTO.quantityOnHand( beer.getQuantityOnHand() );
        beerDTO.price( beer.getPrice() );
        beerDTO.createdData( beer.getCreatedData() );
        beerDTO.updateData( beer.getUpdateData() );

        return beerDTO.build();
    }
}
