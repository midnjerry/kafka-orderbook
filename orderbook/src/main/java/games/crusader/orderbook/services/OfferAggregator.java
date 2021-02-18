package games.crusader.orderbook.services;

import games.crusader.orderbook.messaging.OrderbookPayload;
import games.crusader.orderbook.messaging.TradePayload;
import games.crusader.orderbook.models.OrderBook;
import games.crusader.orderbook.repository.OfferRepository;
import org.springframework.stereotype.Service;

@Service
public class OfferAggregator {
    private final OfferRepository offerRepository;

    public OfferAggregator(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public OrderBook getOrderbook(String marketSymbol) {
        return null;
    }

    public void onTradeUpdate(TradePayload payload) {

    }

    public void onOfferUpdate(OrderbookPayload payload) {

    }
}
