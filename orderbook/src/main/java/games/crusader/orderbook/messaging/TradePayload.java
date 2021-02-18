package games.crusader.orderbook.messaging;

import games.crusader.orderbook.models.TradeUpdate;
import lombok.Data;

import java.util.List;

@Data
public class TradePayload {
    private List<TradeUpdate> deltas;
    private Long sequence;
    private String marketSymbol;
}
