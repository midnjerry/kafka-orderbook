package games.crusader.orderbook.controllers;

import games.crusader.orderbook.models.OrderBook;
import games.crusader.orderbook.services.OfferAggregator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderbookController {

    private final OfferAggregator offerAggregator;

    public OrderbookController(OfferAggregator offerAggregator) {
        this.offerAggregator = offerAggregator;
    }

    @GetMapping("/markets/{marketSymbol}/orderbook")
    public ResponseEntity<OrderBook> getOrderbook(@PathVariable String marketSymbol) {
        return ResponseEntity.ok(offerAggregator.getOrderbook(marketSymbol));
    }
}