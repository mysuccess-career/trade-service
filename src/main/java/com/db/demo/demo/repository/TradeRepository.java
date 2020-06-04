package com.db.demo.demo.repository;

import com.db.demo.demo.entity.TradeStoreEntity;
import org.springframework.stereotype.Component;

import java.util.List;


public interface TradeRepository {

    /**
     * Gets all the current trades from the trade store.
     *
     * @return
     */
    List<TradeStoreEntity> getAllActiveExistingTrades();


    /**
     * Gets all the current trades from the trade store.
     *
     * @return
     */
    List<TradeStoreEntity> getAllExistingTrades();

    /**
     * Checks if the trade is getting expired and updates the same.
     *
     * @param tradeIds
     * @return
     */
    void updateTradeExpiration(List<String> tradeIds);

    /**
     * Saves the given entities and returns the saved entity
     *
     * @param tradeStoreEntities
     * @return
     */
    List<TradeStoreEntity> saveAll(List<TradeStoreEntity> tradeStoreEntities);

    /**
     * Gets all the activeTrades
     *
     * @return
     */
    List<Object[]> getAllActiveTrades();

}
