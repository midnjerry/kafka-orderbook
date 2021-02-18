package games.crusader.orderbook.repository;

import games.crusader.orderbook.models.TradeUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<TradeUpdate, Long> {
}
