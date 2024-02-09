package springframework.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springframework.spring6restmvc.model.Beer;
import springframework.spring6restmvc.services.BeerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController//Rest => Spring now knows it has to return back proper response body (JSON and not HTML)
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    @PatchMapping("{beerId}")
    public ResponseEntity patchById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {

        beerService.patchById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{beerId}")
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {

        beerService.deleteById(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //successful PUT in Postman is with "204 No Content"
    //http://localhost:8080/api/v1/customer/343894bb-2a27-42cf-95b6-1e1e70b2608a I have to add specific id for put request
    @PutMapping("{beerId}")
    public ResponseEntity updateById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {

        beerService.updateBeerById(beerId, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //adding POST operation to accept a new beer into my repository
    @PostMapping//this is cleaner mapping
    //@RequestMapping(method = RequestMethod.POST)//this is second option
    public ResponseEntity handlePost(@RequestBody Beer beer) {//@RB annotation tells Spring to bind the JSON body to a Beer object

        Beer savedBeer = beerService.saveNewBeer(beer);//mimmicking what persistence app would do but this example is without DB so...

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());
        //when a client using the REST API creates a new beer object, it will get back the location of object, including UUID

        return new ResponseEntity(HttpStatus.CREATED);//RE basically gives me the memory to one created for the status
        //comming back, so I am saying that I want this to accept it
        //this is when we get 201 status about successfully save to my "DB"
    }

    @RequestMapping(method = RequestMethod.GET)//maps the path API of one beer to the list of beers
    //so when request comes in, I invoke my service to get the list of beers and that is returned back
    //to the view handler, which in this case is going to be Jackson to produce the JSON response
    //in previous ver it was just "/api/v1/beer"
    public List<Beer> beerList() {
        return beerService.beerList();
    }

    @RequestMapping(value = "{beerId}", method = RequestMethod.GET)//because we added requestMapping on class level
//we can use just only "{beerId}" instead of "/api/v1/beer/{beerId}"
//plus both method are limited to only GET methods so controller will not act on any other request
    public Beer getBearById(@PathVariable("beerId") UUID beerId) {//Spring should match these 2 values "beerId"
        //but don't rely on that so that's why "@PathVariable("beerId")" is added, its mandatory especially if
        //variable names differ

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId);
    }
}
