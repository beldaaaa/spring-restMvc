package springframework.spring6restmvc.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springframework.spring6restmvc.model.Beer;
import springframework.spring6restmvc.services.BeerService;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController//Rest => Spring now knows it has to return back proper response body (JSON and not HTML)
public class BeerController {
    private final BeerService beerService;

    @RequestMapping("/api/v1/beer")//maps the path API of one beer to the list of beers
    //so when request comes in, I invoke my service to get the list of beers and that is returned back
    //to the view handler, wich in this case is going to be Jackson to produce the JSON response
    public List<Beer> beerList() {
        return beerService.beerList();
    }

    public Beer getBearById(UUID id) {

        log.debug("Get Beer by Id - in controller");

        return beerService.getBearById(id);
    }
}
