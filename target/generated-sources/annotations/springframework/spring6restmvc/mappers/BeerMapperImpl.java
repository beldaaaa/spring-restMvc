package springframework.spring6restmvc.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.entities.BeerAudit;
import springframework.spring6restmvc.models.BeerDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-25T14:29:41+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Azul Systems, Inc.)"
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
        beer.createdDate( dto.getCreatedDate() );
        beer.lastModifiedDate( dto.getLastModifiedDate() );

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
        beerDTO.createdDate( beer.getCreatedDate() );
        beerDTO.lastModifiedDate( beer.getLastModifiedDate() );

        return beerDTO.build();
    }

    @Override
    public BeerAudit beerToBeerAudit(Beer beer) {
        if ( beer == null ) {
            return null;
        }

        BeerAudit.BeerAuditBuilder beerAudit = BeerAudit.builder();

        beerAudit.id( beer.getId() );
        beerAudit.version( beer.getVersion() );
        beerAudit.beerName( beer.getBeerName() );
        beerAudit.beerStyle( beer.getBeerStyle() );
        beerAudit.upc( beer.getUpc() );
        beerAudit.quantityOnHand( beer.getQuantityOnHand() );
        beerAudit.price( beer.getPrice() );
        beerAudit.createdDate( beer.getCreatedDate() );
        beerAudit.lastModifiedDate( beer.getLastModifiedDate() );

        return beerAudit.build();
    }
}
