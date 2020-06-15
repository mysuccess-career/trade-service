package com.db.demo.demo.controller;

import com.db.demo.demo.dto.ResponseTemplate;
import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.dto.TradeResponseDto;
import com.db.demo.demo.exception.TradeServiceException;
import com.db.demo.demo.manager.impl.TradeManagerImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.*;

@ExtendWith({MockitoExtension.class})
class TradeControllerTest {

    @Mock
    TradeManagerImpl tradeManager;

    @InjectMocks
    TradeController tradeController;

    private static TradeDto activeTrade = null;
    private static TradeDto expiredTrade = null;
    private static List<TradeDto> activeTrades = null;
    private static List<TradeDto> allTrades = null;

    @BeforeAll
    static void setup() {
        activeTrades = new ArrayList<>();
        allTrades = new ArrayList<>();

        activeTrade = new TradeDto();
        activeTrade.setTradeId("T1");
        activeTrade.setExpired("N");
        activeTrade.setMaturity(new Date());
        activeTrade.setVersion(1);
        activeTrades.add(activeTrade);

        expiredTrade = new TradeDto();
        expiredTrade.setTradeId("T2");
        expiredTrade.setExpired("Y");
        expiredTrade.setVersion(2);

        Calendar currentDate = Calendar.getInstance();
        currentDate.add(currentDate.DAY_OF_YEAR, -1);
        expiredTrade.setMaturity(currentDate.getTime());
        allTrades.addAll(activeTrades);
        allTrades.add(expiredTrade);
    }

    @AfterAll
    static void tearDown() {
        activeTrades = null;
        allTrades = null;
    }

    @Test
    @DisplayName("Test to get active trades")
    void geActiveExistingTrades() {
        //GIVEN
        Mockito.when(tradeManager.getActiveExistingTrades()).thenReturn(activeTrades);

        //WHEN
        ResponseEntity<ResponseTemplate> responseEntity = tradeController.geActiveExistingTrades();

        //THEN
        ResponseTemplate responseTemplate = responseEntity.getBody();

        List<TradeDto> tradeDtos = (List<TradeDto>) responseTemplate.getData();
        Assertions.assertNotNull(tradeDtos);//TODO more assertions can be added for data check
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        //VERIFY
        Mockito.verify(tradeManager).getActiveExistingTrades();
    }

    @Test
    @DisplayName("Test to get all existing trades")
    void getAllExistingTrades() {
        //GIVEN
        Mockito.when(tradeManager.getAllExistingTrades()).thenReturn(allTrades);

        //WHEN
        ResponseEntity<ResponseTemplate> allTrades = tradeController.getAllExistingTrades();

        //THEN
        ResponseTemplate responseTemplate = allTrades.getBody();
        List<TradeDto> tradeDTOs = (List<TradeDto>) responseTemplate.getData();
        Assertions.assertNotNull(tradeDTOs);
        Assertions.assertEquals(2, tradeDTOs.size());//TODO more assertions can be added for data check
        Assertions.assertEquals(HttpStatus.OK, allTrades.getStatusCode());

        //VERIFY
        Mockito.verify(tradeManager).getAllExistingTrades();
    }

    @Test
    @DisplayName("Test to create trade")
    void updateTrade() throws Exception {
        //GIVEN
        TradeDto tradeUpdatesDto = activeTrade;
        tradeUpdatesDto.setVersion(3);
        TradeResponseDto tradeResponseDto = new TradeResponseDto();
        Mockito.when(tradeManager.updateTrade(Collections.singletonList(tradeUpdatesDto))).thenReturn(tradeResponseDto);

        //WHEN
        ResponseEntity<ResponseTemplate> responseEntity = tradeController.updateTrade(Collections.singletonList(tradeUpdatesDto));

        //THEN
        ResponseTemplate responseTemplate = responseEntity.getBody();

        Assertions.assertNotNull(responseTemplate);
        Assertions.assertNotNull(responseEntity.getBody());//TODO more assertions can be added for data check
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //VERIFY
        Mockito.verify(tradeManager).updateTrade(Collections.singletonList(tradeUpdatesDto));
    }

    @Test
    @DisplayName("Test to create trade for version validation")
    void updateTradeForException() throws TradeServiceException, ParseException {
        //GIVEN
        TradeDto tradeDto = TradeDto.builder().tradeId("T1").bookId("B1").counterPartyId("CP-1").build();
        Mockito.when(tradeManager.updateTrade(Collections.singletonList(tradeDto))).thenThrow(TradeServiceException.class);

        //WHEN //THEN
        Assertions.assertThrows(TradeServiceException.class, () -> tradeController.updateTrade(Collections.singletonList(tradeDto)));

        //VERIFY
        Mockito.verify(tradeManager).updateTrade(Collections.singletonList(tradeDto));
    }
}