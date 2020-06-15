package com.db.demo.demo.controller;

import com.db.demo.demo.dto.ResponseTemplate;
import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.dto.TradeResponseDto;
import com.db.demo.demo.exception.TradeServiceException;
import com.db.demo.demo.manager.TradeManager;
import com.db.demo.demo.uri.TradeUri;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.db.demo.demo.uri.TradeUri.TRADES;
import static com.db.demo.demo.uri.TradeUri.VERSION;

/**
 * @author Savitha
 */

@RequestMapping(TRADES)
@RestController
public class TradeController {

    private static final Logger LOGGER = LogManager.getLogger(TradeController.class);

    @Autowired
    TradeManager tradeManager;

    @GetMapping(value = TradeUri.ACTIVE, params = VERSION)
    @ResponseBody
    public ResponseEntity<ResponseTemplate> geActiveExistingTrades() {
        LOGGER.info("Entry in get geActiveExistingTrades method");
        List<TradeDto> activeTrades = tradeManager.getActiveExistingTrades();
        return new ResponseEntity<>(ResponseTemplate.builder().data(activeTrades).build(), HttpStatus.OK);
    }

    @GetMapping(value = TradeUri.ALL, params = VERSION)
    @ResponseBody
    public ResponseEntity<ResponseTemplate> getAllExistingTrades() {
        LOGGER.info("Entry in getAllExistingTrades method");
        List<TradeDto> activeTrades = tradeManager.getAllExistingTrades();
        return new ResponseEntity<>(ResponseTemplate.builder().data(activeTrades).build(), HttpStatus.OK);
    }

    @PostMapping(params = VERSION)
    @ResponseBody
    public ResponseEntity<ResponseTemplate> updateTrade(@Validated @RequestBody List<TradeDto> tradeDtos) throws TradeServiceException, ParseException {
        LOGGER.info("Entry in updateTrade method");
        TradeResponseDto tradeResponseDto = tradeManager.updateTrade(tradeDtos);
        LOGGER.debug("output:{}", tradeResponseDto);
        return new ResponseEntity<>(ResponseTemplate.builder().data(tradeResponseDto).build(), HttpStatus.OK);
    }

    /*This API can be used if manually we need run the check on maturity date and set the expiration flag*/
    @PutMapping(value = TradeUri.ARCHIVE, params = VERSION)
    ResponseEntity<Object> archiveTrades() throws ParseException {
        LOGGER.info("Entry in archive Trade method");
        List<String> archivedTrades = tradeManager.updateTradeExpiration();
        Map<String, List<String>> responseMap = new HashMap<>();
        responseMap.put("ExpiredTradeIds", archivedTrades);
        return new ResponseEntity<>(ResponseTemplate.builder().data(responseMap).build(), HttpStatus.OK);
    }
}

