package springframework.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import springframework.spring6restmvc.model.Beer;
import springframework.spring6restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, Beer> beerMap;// just for example as substitute for DB
    //so its initialized in ctor and generates data

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();
        Beer beer1 = Beer.builder()//builder pattern saves quite of code (no creating new object, no setters and returning value then,...)
                .id(UUID.randomUUID())
                .version(1)
                .beerName("12")
                .beerStyle(BeerStyle.PILSNER)
                .upc("456")
                .price(new BigDecimal("25.90"))
                .quantityOnHand(40)
                .createdData(LocalDateTime.now())
                .updateData(LocalDateTime.now())
                .build();
        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("10")
                .beerStyle(BeerStyle.STOUT)
                .upc("444")
                .price(new BigDecimal("12.90"))
                .quantityOnHand(120)
                .createdData(LocalDateTime.now())
                .updateData(LocalDateTime.now())
                .build();
        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("1000")
                .beerStyle(BeerStyle.IPA)
                .upc("99")
                .price(new BigDecimal("50.90"))
                .quantityOnHand(12)
                .createdData(LocalDateTime.now())
                .updateData(LocalDateTime.now())
                .build();
        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<Beer> beerList() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<Beer> getBeerById(UUID id) {

        log.debug("Get Beer Id in service was called");

        return Optional.ofNullable(beerMap.get(id));
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        Beer savedBeer = Beer.builder()//here we are again mimmicking what the persistent store is going to do here
                .id(UUID.randomUUID())
                .createdData(LocalDateTime.now())
                .updateData(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();
        beerMap.put(savedBeer.getId(), savedBeer);
        return savedBeer;
        //so this persistence method takes in the object that was created
        //I am assigning properties as if I was the DB, but I am taking from the object that I passed in the property
        //and then I am persissting it to my map and then returning back the new saveBeer object
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setPrice(beer.getPrice());
        existing.setUpc(beer.getUpc());
        existing.setQuantityOnHand(beer.getQuantityOnHand());

        beerMap.put(existing.getId(), existing);
    }

    @Override
    public void deleteById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchById(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);

        if (StringUtils.hasText(beer.getBeerName())) {
            existing.setBeerName(beer.getBeerName());
        }
        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }
        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }
        if (beer.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }
    }
}
