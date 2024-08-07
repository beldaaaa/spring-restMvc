package springframework.spring6restmvc.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.spring6restmvc.services.BeerOrderService;
import springframework.springrestmvcapi.model.BeerOrderCreateDTO;
import springframework.springrestmvcapi.model.BeerOrderDTO;
import springframework.springrestmvcapi.model.BeerOrderUpdateDTO;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BeerOrderController {
    static final String BEER_ORDER_PATH = "/api/v1/beerorder";
    static final String BEER_ORDER_PATH_ID = BEER_ORDER_PATH + "/{beerOrderId}";

    private final BeerOrderService beerOrderService;

    @GetMapping(BEER_ORDER_PATH_ID)
    public BeerOrderDTO getBeerOrderById(@PathVariable UUID beerOrderId) {
        return beerOrderService.findById(beerOrderId).orElseThrow(NotFoundException::new);
    }

    @GetMapping(BEER_ORDER_PATH)
    public Page<BeerOrderDTO> listOrders(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        return beerOrderService.listOrders(pageNumber, pageSize);
    }

    @PostMapping(BEER_ORDER_PATH)
    public ResponseEntity<Void> createBeerOrder(@RequestBody BeerOrderCreateDTO beerOrderCreateDTO) {
        BeerOrder savedBeerOrder = beerOrderService.createBeerOrder(beerOrderCreateDTO);

        return ResponseEntity.created(URI.create(BEER_ORDER_PATH + "/" + savedBeerOrder.getId().toString())).build();
    }

    @PutMapping(BEER_ORDER_PATH_ID)
    public ResponseEntity<BeerOrderDTO> updateOrder(@PathVariable UUID beerOrderId, @RequestBody BeerOrderUpdateDTO beerOrderUpdateDTO) {
        return ResponseEntity.ok(beerOrderService.updateOrder(beerOrderId, beerOrderUpdateDTO));
    }

    @DeleteMapping(BEER_ORDER_PATH_ID)
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID beerOrderId) {
        beerOrderService.deleteOrder(beerOrderId);
        return ResponseEntity.noContent().build();
    }
}
