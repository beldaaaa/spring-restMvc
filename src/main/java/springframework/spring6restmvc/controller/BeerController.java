package springframework.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(method = RequestMethod.GET)//maps the path API of one beer to the list of beers
    //so when request comes in, I invoke my service to get the list of beers and that is returned back
    //to the view handler, wich in this case is going to be Jackson to produce the JSON response
    //in previous ver it was just "/api/v1/beer"
    public List<Beer> beerList() {
        return beerService.beerList();
    }
@RequestMapping(value = "{beerId}",method = RequestMethod.GET)//because we added requestMapping on class level
//we can use just only "{beerId}" instead of "/api/v1/beer/{beedId}"
//plus both method are limited to only GET methods so controller will not act on any other request
    public Beer getBearById(@PathVariable("beerId") UUID beerId) {//Spring should matchthese 2 values "beerId"
        //but dont rely on that so thats why "@PathVariable("beerId")" is added

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId);
    }
}
