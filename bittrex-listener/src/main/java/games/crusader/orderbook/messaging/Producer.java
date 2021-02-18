package games.crusader.orderbook.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String ORDERBOOK_TOPIC = "orderbook";
    private static final String TRADE_TOPIC = "trade";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendTradeUpdate(String message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(TRADE_TOPIC, "Trade", message);
    }

    public void sendOrderUpdate(String message) {
        logger.info(String.format("#### -> Producing message -> %s", message));
        this.kafkaTemplate.send(ORDERBOOK_TOPIC, "Orderbook", message);
    }
}
