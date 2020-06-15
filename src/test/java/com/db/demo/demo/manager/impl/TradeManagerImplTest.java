package com.db.demo.demo.manager.impl;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.dto.TradeResponseDto;
import com.db.demo.demo.entity.TradeStoreEntity;
import com.db.demo.demo.exception.TradeServiceException;
import org.junit.jupiter.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class TradeManagerImplTest {

    private static List<TradeDto> activeTrades = null;
    private static List<TradeDto> allTrades = null;
    private static List<TradeStoreEntity> tradeStoreEntities = null;
    private static TradeDto existingTradeDto = new TradeDto();
    private static TradeDto activeTrade = new TradeDto();

    @Autowired
    private TradeManagerImpl tradeManager;

    @BeforeAll
    private static void setup() {
        activeTrades = new ArrayList<>();
        allTrades = new ArrayList<>();
        tradeStoreEntities = new ArrayList<>();
        activeTrade.setTradeId("J1");
        activeTrade.setExpired("N");
        activeTrade.setMaturity(new Date());
        activeTrade.setVersion(1);
        activeTrade.setCreatedDate(new Date());
        activeTrade.setBookId("JB1");
        activeTrades.add(activeTrade);

        TradeDto expiredTrade = new TradeDto();
        expiredTrade.setTradeId("J2");
        expiredTrade.setExpired("Y");
        expiredTrade.setVersion(2);
        expiredTrade.setCreatedDate(new Date());
        expiredTrade.setBookId("JB2");

        Calendar expiredDate = Calendar.getInstance();
        expiredDate.add(expiredDate.DAY_OF_YEAR, -1);
        expiredTrade.setMaturity(expiredDate.getTime());
        allTrades.addAll(activeTrades);
        allTrades.add(expiredTrade);

        existingTradeDto.setTradeId("T2");
        existingTradeDto.setVersion(2);
        existingTradeDto.setCounterPartyId("CP-1");
        existingTradeDto.setBookId("B1");
        existingTradeDto.setCreatedDate(new Date());
        Calendar validDate = Calendar.getInstance();
        validDate.add(validDate.DAY_OF_YEAR, +1);
        existingTradeDto.setMaturity(validDate.getTime());
        existingTradeDto.setExpired("N");

        activeTrades.stream().forEach(tradeDto -> {
            TradeStoreEntity tradeStoreEntity = new TradeStoreEntity();
            BeanUtils.copyProperties(tradeDto, tradeStoreEntity);
            tradeStoreEntities.add(tradeStoreEntity);
        });
    }

    @AfterAll
    private static void tearDown() {
        activeTrades = null;
        allTrades = null;
        tradeStoreEntities = null;
        existingTradeDto = null;
        activeTrade = null;
    }

    @Test
    @DisplayName("Gets only the active Trades")
    void getActiveExistingTrades() {
        //WHEN
        List<TradeDto> tradeStoreEntities = tradeManager.getActiveExistingTrades();

        //THEN
        Assertions.assertNotNull(tradeStoreEntities);
    }

    @Test
    @DisplayName("Gets all the existing Trades")
    void getAllExistingTrades() {
        //WHEN
        List<TradeDto> tradeStoreEntities = tradeManager.getAllExistingTrades();

        //THEN
        Assertions.assertNotNull(tradeStoreEntities);
    }

    @Test
    @DisplayName("Update existing trade with lower version")
    void updateTradeLowerTradeVersionTest() {
        //GIVEN
        TradeDto tradeDto = existingTradeDto;
        int version = tradeDto.getVersion() - 1;

        //WHEN
        tradeDto.setVersion(version);

        //THEN
//        Assertions.assertThrows(TradeServiceException.class, () -> tradeManager.updateTrade(tradeDto));
    }

    @Test
    @DisplayName("Create Trade with higher version")
    void updateTradeWithValidMaturityDateTest() throws TradeServiceException, ParseException {
        //GIVEN
        TradeDto tradeDto = existingTradeDto;
        int version = tradeDto.getVersion() + 2;
        tradeDto.setVersion(version);

        //WHEN
     /*   TradeResponseDto tradeResponseDto = tradeManager.updateTrade(tradeDto);

        //THEN
        Assertions.assertNotNull(tradeResponseDto.getTradeDtos());
        Assertions.assertTrue(tradeResponseDto.getNoOfTradesCreated() == 1);*/
    }

    @Test
    @DisplayName("Update existing trade with valid data")
    void updateTradeTest() throws TradeServiceException, ParseException {
        //GIVEN
        TradeDto tradeDto = existingTradeDto;

        //WHEN
        TradeResponseDto tradeResponseDto = tradeManager.updateTrade(Collections.singletonList(tradeDto));

        //THEN
        Assertions.assertNotNull(tradeResponseDto.getTradeDtos());
        Assertions.assertTrue(tradeResponseDto.getNoOfTradesUpdated() == 1);
    }

    @Test
    @DisplayName("Update existing trade with current date")
    void updateTradeWithCurrentDateTest() throws TradeServiceException, ParseException {
        //GIVEN
        TradeDto tradeDto = existingTradeDto;

        Date dateTrade = new SimpleDateFormat("dd/MM/yyyy").parse("15/06/2020");
         tradeDto.setMaturity(dateTrade);
        //WHEN
        TradeResponseDto tradeResponseDto = tradeManager.updateTrade(Collections.singletonList(tradeDto));
        System.out.println("tradeResponseDto" + tradeResponseDto);
        //THEN
        Assertions.assertNotNull(tradeResponseDto.getTradeDtos());
        Assertions.assertTrue(tradeResponseDto.getNoOfTradesUpdated() == 1);
    }

   /* @Test
    @DisplayName("Update trade with expired maturity date")
    void updateTradeWithExpiredMaturityDateTest() throws TradeServiceException, ParseException {
        //GIVEN
        Calendar expiredDate = Calendar.getInstance();
        expiredDate.add(expiredDate.DAY_OF_YEAR, -1);
        TradeDto expiredTrade = new TradeDto();
        expiredTrade.setMaturity(expiredDate.getTime());

        //WHEN
        TradeResponseDto tradeResponseDto = tradeManager.updateTrade(expiredTrade);

        //THEN
        Assertions.assertTrue(tradeResponseDto.isTradeMaturityExpired());
    }*/

    @Test
    void updateTradeExpiration() throws ParseException {
        //WHEN
        List<String> tradeIds = tradeManager.updateTradeExpiration();

        //THEN
        Assertions.assertNotNull(tradeIds);
    }

}