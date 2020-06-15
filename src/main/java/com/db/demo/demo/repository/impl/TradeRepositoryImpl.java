package com.db.demo.demo.repository.impl;

import com.db.demo.demo.entity.TradeStoreEntity;
import com.db.demo.demo.repository.TradeRepository;
import com.db.demo.demo.repository.jpa.TradeRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Savitha
 */

@Repository
public class TradeRepositoryImpl implements TradeRepository {

    public static final String ACTIVE_TRADE_STATUS = "N";

    @Autowired
    TradeRepositoryJpa tradeRepositoryJpa;

    @Override
    public List<TradeStoreEntity> getAllActiveExistingTrades() {
        return tradeRepositoryJpa.findActiveTradesData();
    }

    @Override
    public List<TradeStoreEntity> getAllExistingTrades() {
        return tradeRepositoryJpa.findAll();
    }

    @Override
    public void updateTradeExpiration(List<String> tradeIds) {
        tradeRepositoryJpa.updateTradesForExpiry(tradeIds);
    }

    @Override
    public List<TradeStoreEntity> saveAll(List<TradeStoreEntity> tradeStoreEntities) {
        return tradeRepositoryJpa.saveAll(tradeStoreEntities);
    }

    @Override
    public List<Object[]> getAllActiveTrades() {
        return tradeRepositoryJpa.findActiveTrades(ACTIVE_TRADE_STATUS);
    }

}
