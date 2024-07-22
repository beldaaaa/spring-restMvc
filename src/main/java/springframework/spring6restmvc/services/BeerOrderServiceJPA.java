package springframework.spring6restmvc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import springframework.spring6restmvc.controllers.NotFoundException;
import springframework.spring6restmvc.entities.BeerOrder;
import springframework.spring6restmvc.entities.BeerOrderLine;
import springframework.spring6restmvc.entities.BeerOrderShipment;
import springframework.spring6restmvc.entities.Customer;
import springframework.spring6restmvc.mappers.BeerOrderMapper;
import springframework.spring6restmvc.repositories.BeerOrderRepository;
import springframework.spring6restmvc.repositories.BeerRepository;
import springframework.spring6restmvc.repositories.CustomerRepository;
import springframework.springrestmvcapi.model.BeerOrderCreateDTO;
import springframework.springrestmvcapi.model.BeerOrderDTO;
import springframework.springrestmvcapi.model.BeerOrderUpdateDTO;
import springframework.springrestmvcapi.model.events.OrderPlacedEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerOrderServiceJPA implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Optional<BeerOrderDTO> findById(UUID beerOrderId) {
        return Optional.ofNullable(beerOrderMapper.beerOrderToBeerOrderDto(beerOrderRepository.findById(beerOrderId)
                .orElse(null)));
    }

    @Override
    public Page<BeerOrderDTO> listOrders(Integer pageNumber, Integer pageSize) {

        if (pageNumber == null || pageNumber < 0) {//page starts with index 0
            pageNumber = 0;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 99;
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return beerOrderRepository.findAll(pageRequest).map(beerOrderMapper::beerOrderToBeerOrderDto);
    }

    @Override
    public BeerOrder createBeerOrder(BeerOrderCreateDTO beerOrderCreateDTO) {
        Customer customer = customerRepository.findById(beerOrderCreateDTO.getCustomerId())
                .orElseThrow(NotFoundException::new);

        Set<BeerOrderLine> beerOrderLines = new HashSet<>();

        beerOrderCreateDTO.getBeerOrderLinesCreateDTO().forEach(beerOrderLine -> beerOrderLines.add(BeerOrderLine.builder()
                .beer(beerRepository.findById(beerOrderLine.getBeerId())
                        .orElseThrow(NotFoundException::new))
                .orderQuantity(beerOrderLine.getOrderQuantity())
                .build()));
        return beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .beerOrderLines(beerOrderLines)
                .customerRef(beerOrderCreateDTO.getCustomerRef())
                .build());
    }

    @Override
    public BeerOrderDTO updateOrder(UUID beerOrderId, BeerOrderUpdateDTO beerOrderUpdateDTO) {
        BeerOrder order = beerOrderRepository.findById(beerOrderId).orElseThrow(NotFoundException::new);

        order.setCustomer(customerRepository.findById(beerOrderUpdateDTO.getCustomerId()).orElseThrow(NotFoundException::new));
        order.setCustomerRef(beerOrderUpdateDTO.getCustomerRef());

        beerOrderUpdateDTO.getBeerOrderLinesUpdateDTO().forEach(beerOrderLine -> {

            if (beerOrderLine.getBeerId() != null) {
                BeerOrderLine foundLine = order.getBeerOrderLines().stream()
                        .filter(beerOrderLine1 -> beerOrderLine1.getId().equals(beerOrderLine.getLineId()))
                        .findFirst().orElseThrow(NotFoundException::new);
                foundLine.setBeer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new));
                foundLine.setOrderQuantity(beerOrderLine.getOrderQuantity());
                foundLine.setQuantityAllocated(beerOrderLine.getQuantityAllocated());
            } else {
                order.getBeerOrderLines().add(BeerOrderLine.builder()
                        .beer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new))
                        .orderQuantity(beerOrderLine.getOrderQuantity())
                        .quantityAllocated(beerOrderLine.getQuantityAllocated())
                        .build());
            }
        });

        if (beerOrderUpdateDTO.getShipmentUpdateDTO() != null && beerOrderUpdateDTO.getShipmentUpdateDTO().getTrackingNumber() != null) {
            if (order.getBeerOrderShipment() == null) {
                order.setBeerOrderShipment(BeerOrderShipment.builder().trackingNumber(beerOrderUpdateDTO.getShipmentUpdateDTO().getTrackingNumber()).build());
            } else {
                order.getBeerOrderShipment().setTrackingNumber(beerOrderUpdateDTO.getShipmentUpdateDTO().getTrackingNumber());
            }
        }

        BeerOrderDTO beerOrderDTO = beerOrderMapper.beerOrderToBeerOrderDto(beerOrderRepository.save(order));

        if(beerOrderUpdateDTO.getPaymentAmount() != null) {
            eventPublisher.publishEvent(OrderPlacedEvent.builder()
                    .beerOrderDTO(beerOrderDTO));
        }

        return beerOrderDTO;
    }

    @Override
    public void deleteOrder(UUID beerOrderId) {
        if (beerOrderRepository.existsById(beerOrderId)) {
            beerOrderRepository.deleteById(beerOrderId);
        } else {
            throw new NotFoundException();
        }
    }
}

