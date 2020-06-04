package com.db.demo.demo.repository.impl;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.entity.TradeStoreEntity;
import com.db.demo.demo.repository.TradeRepository;
import com.db.demo.demo.repository.jpa.TradeRepositoryJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class TradeRepositoryImplTest implements ApplicationRunner {

    private static List<TradeStoreEntity> tradeStoreEntities = null;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradeRepositoryJpa tradeRepositoryJpa;

    @BeforeAll
    static void setup() {
        List<TradeDto> activeTrades = new ArrayList<>();
        List<TradeDto> allTrades = new ArrayList<>();
        tradeStoreEntities = new ArrayList<>();
        TradeDto activeTrade = new TradeDto();
        activeTrade.setTradeId("T1");
        activeTrade.setExpired("N");
        activeTrade.setMaturity(new Date());
        activeTrade.setVersion(1);
        activeTrade.setCreatedDate(new Date());
        activeTrade.setBookId("B1");
        activeTrades.add(activeTrade);

        TradeDto expiredTrade = new TradeDto();
        expiredTrade.setTradeId("T2");
        expiredTrade.setExpired("Y");
        expiredTrade.setMaturity(new Date());
        expiredTrade.setVersion(1);
        expiredTrade.setCreatedDate(new Date());
        expiredTrade.setBookId("B2");
        expiredTrade.setVersion(2);

        Calendar currentDate = Calendar.getInstance();
        currentDate.add(currentDate.DAY_OF_YEAR, -1);
        expiredTrade.setMaturity(currentDate.getTime());
        allTrades.addAll(activeTrades);
        allTrades.add(expiredTrade);

        activeTrades.stream().forEach(tradeDto -> {
            TradeStoreEntity tradeStoreEntity = new TradeStoreEntity();
            BeanUtils.copyProperties(tradeDto, tradeStoreEntity);
            tradeStoreEntities.add(tradeStoreEntity);
        });
    }

    List<TradeStoreEntity> savePreCreatedEntities() {
        return tradeRepositoryJpa.saveAll(tradeStoreEntities);
    }


    @Test
    void getAllActiveExistingTrades() {
        savePreCreatedEntities();
        List<TradeStoreEntity> tradeStoreEntities = tradeRepository.getAllExistingTrades();
        Assertions.assertNotNull(tradeStoreEntities);
    }

    @Test
    void getAllExistingTrades() {
    }

    @Test
    void updateTradeExpiration() {
    }

    @Test
    void saveAll() {
    }

    @Test
    void getAllActiveTrades() {
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}