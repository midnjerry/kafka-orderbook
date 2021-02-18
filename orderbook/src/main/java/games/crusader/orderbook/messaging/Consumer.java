package games.crusader.orderbook.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import games.crusader.orderbook.services.OfferAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {
    ObjectMapper objectMapper;
    OfferAggregator offerAggregator;

    public Consumer(ObjectMapper objectMapper, OfferAggregator offerAggregator){
        this.objectMapper = objectMapper;
        this.offerAggregator = offerAggregator;
    }

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "trade", groupId = "group_id")
    public void consume(String data) throws IOException {
        TradePayload payload = objectMapper.readValue(data, TradePayload.class);
        offerAggregator.onTradeUpdate(payload);
        logger.info(String.format("#### -> Consumed message -> %s", payload));
    }

    @KafkaListener(topics = "orderbook", groupId = "group_id")
    public void consumeOrderbook(String data) throws IOException {
        OrderbookPayload payload = objectMapper.readValue(data, OrderbookPayload.class);
        offerAggregator.onOfferUpdate(payload);
        logger.info(String.format("#### -> Consumed message -> %s", payload));
    }
}