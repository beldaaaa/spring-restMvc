package springframework.spring6restmvc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import springframework.spring6restmvc.entities.Beer;
import springframework.spring6restmvc.events.BeerCreatedEvent;
import springframework.spring6restmvc.events.BeerDeletedEvent;
import springframework.spring6restmvc.events.BeerPatchedEvent;
import springframework.spring6restmvc.events.BeerUpdatedEvent;
import springframework.spring6restmvc.mappers.BeerMapper;
import springframework.spring6restmvc.repositories.BeerRepository;
import springframework.springrestmvcapi.model.BeerDTO;
import springframework.springrestmvcapi.model.BeerStyle;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDTO> beerPage(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPagerequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = pageBeerByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = pageBeerByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = pageBeerByNameAndStyle(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beerPage.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beerPage.map(beerMapper::beerToBeerDto);
    }

    public PageRequest buildPagerequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }
        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    Page<Beer> pageBeerByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%",
                beerStyle, pageable);
    }

    Page<Beer> pageBeerByName(String beerName, Pageable pageable) {
        // + wildcard search characters for SQL again
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
    }

    Page<Beer> pageBeerByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    @Cacheable(cacheNames = "beerCache", key = "#id")//key is not mandatory here
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        Objects.requireNonNull(cacheManager.getCache("beerListCache")).clear();
        var saveBeer = beerRepository.save(beerMapper.beerDtoToBeer(beer));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        applicationEventPublisher.publishEvent(new BeerCreatedEvent(saveBeer, authentication));
        return beerMapper.beerToBeerDto(saveBeer);
    }

    //due to stall cache, I need to do a different approach or refactor code and split operations into separate classes
//@Caching(evict = {
//        @CacheEvict(cacheNames="beerCache",key ="#beerId"),
//        @CacheEvict(cacheNames = "beerListCache")
//})
    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
        clearCache(beerId);
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();//workaround due to lambda function usage
        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setVersion(beer.getVersion());

            var savedBeer = beerRepository.save(foundBeer);
            var auth = SecurityContextHolder.getContext().getAuthentication();

            applicationEventPublisher.publishEvent(new BeerUpdatedEvent(savedBeer, auth));

            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(savedBeer)));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID beerId) {
        clearCache(beerId);
        if (beerRepository.existsById(beerId)) {

            var auth = SecurityContextHolder.getContext().getAuthentication();

            applicationEventPublisher.publishEvent(new BeerDeletedEvent(Beer.builder().id(beerId).build(), auth));

            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchById(UUID beerId, BeerDTO beer) {
        clearCache(beerId);
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();
        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());

            var savedBeer = beerRepository.save(foundBeer);
            var auth = SecurityContextHolder.getContext().getAuthentication();

            applicationEventPublisher.publishEvent(new BeerPatchedEvent(savedBeer, auth));

            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(savedBeer)));
        }, () -> atomicReference.set(Optional.empty()));
        return atomicReference.get();
    }

    private void clearCache(UUID beerId) {
        Objects.requireNonNull(cacheManager.getCache("beerCache")).evict(beerId);
        Objects.requireNonNull(cacheManager.getCache("beerListCache")).clear();
    }
}
