package springframework.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springframework.spring6restmvc.model.Beer;
import springframework.spring6restmvc.services.BeerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController//Rest => Spring now knows it has to return back proper response body (JSON and not HTML)
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final BeerService beerService;

    //adding POST operation to accept a new beer into my repository
    @PostMapping//this is cleaner mapping
    //@RequestMapping(method = RequestMethod.POST)//this is second option
    public ResponseEntity handlePost(@RequestBody Beer beer){//@BR annotation tells Spring to bind the JSON body to a Beer object
        Beer savedBeer = beerService.saveNewBeer(beer);//mimmicking what persistence app would do but this example is without DB so...

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
