package springframework.spring6restmvc.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springframework.spring6restmvc.services.BeerService;
import springframework.springrestmvcapi.model.BeerDTO;
import springframework.springrestmvcapi.model.BeerStyle;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController//Rest => Spring now knows it has to return proper response body (JSON and not HTML)
public class BeerController {
    private final BeerService beerService;
    static final String BEER_PATH = "/api/v1/beer";
    static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer) {

        beerService.patchById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId) {

        if (!beerService.deleteById(beerId)) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDTO beer) {

        if (beerService.updateBeerById(beerId, beer).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity handlePost(@Validated @RequestBody BeerDTO beer) {
//@Validated tells SpringMVC to go in DTO object, it must be valid according to validation constraints
        BeerDTO savedBeer = beerService.saveNewBeer(beer);//mimicking what persistence app would do but this example is without DB so...

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEER_PATH + "/" + savedBeer.getId().toString());
        //when a client using the REST API creates a new beer object, it will get back the location of object, including UUID

        return new ResponseEntity(headers, HttpStatus.CREATED);//RE basically gives me the memory to one created for the status
        //coming back, so I am saying that I want this to accept it
        //this is when I get 201 status about successfully save to my "DB"
    }

    @GetMapping(BEER_PATH)//maps the path API of one beer to the list of beers
    //so when request comes in, I invoke my service to get the list of beers and that is returned back
    //to the view handler, which in this case is going to be Jackson to produce the JSON response
    public Page<BeerDTO> beerPage(@RequestParam(required = false) String beerName,
                                  @RequestParam(required = false) BeerStyle beerStyle,
                                  @RequestParam(required = false) Boolean showInventory,
                                  @RequestParam(required = false) Integer pageNumber,
                                  @RequestParam(required = false) Integer pageSize) {
        //Request and required is due to compatibility with BeerIT
        return beerService.beerPage(beerName, beerStyle, showInventory, pageNumber, pageSize);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBearById(@PathVariable("beerId") UUID beerId) {//Spring should match these 2 values "beerId"
        //but don't rely on that so that's why "@PathVariable("beerId")" is added, its mandatory especially if
        //variable names differ

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }
}
