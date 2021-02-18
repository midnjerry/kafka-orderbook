package games.crusader.orderbook.messaging;

import games.crusader.orderbook.models.Offer;
import lombok.Data;

import java.util.List;

@Data
public class OrderbookPayload {
    private String marketSymbol;
    private int depth;
    private long sequence;
    List<Offer> bidDeltas;
    List<Offer> askDeltas;

}
