package games.crusader.orderbook.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class TradeUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String marketSymbol;
    private String executedAt;
    private Double quantity;
    private Double rate;
    private Side takerSide;
}
