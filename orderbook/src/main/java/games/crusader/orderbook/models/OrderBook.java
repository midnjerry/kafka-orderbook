package games.crusader.orderbook.models;

import lombok.Data;

import java.util.List;

@Data
public class OrderBook {
    private List<List<Double>> bids;
    private List<List<Double>> asks;


}
