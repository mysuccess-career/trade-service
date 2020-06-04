package com.db.demo.demo.controller;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.dto.TradeResponseDto;
import com.db.demo.demo.manager.TradeManager;
import com.db.demo.demo.uri.TradeUri;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> geActiveExistingTrades() {
        LOGGER.info("Entry in get geActiveExistingTrades method");
        List<TradeDto> activeTrades = tradeManager.getActiveExistingTrades();
        return new ResponseEntity<>(activeTrades, HttpStatus.OK);
    }

    @GetMapping(value = TradeUri.ALL, params = VERSION)
    public ResponseEntity<Object> getAllExistingTrades() {
        LOGGER.info("Entry in getAllExistingTrades method");
        List<TradeDto> activeTrades = tradeManager.getAllExistingTrades();
        return new ResponseEntity<>(activeTrades, HttpStatus.OK);
    }

    @PostMapping(params = VERSION)
    public ResponseEntity<Object> updateTrade(@Validated @RequestBody TradeDto tradeDto) throws Exception {
        LOGGER.info("Entry in updateTrade method");
        LOGGER.debug("input:{}",tradeDto);
        TradeResponseDto tradeResponseDto = tradeManager.updateTrade(tradeDto);
        LOGGER.debug("output:{}",tradeResponseDto);
        return new ResponseEntity<>(tradeResponseDto, HttpStatus.OK);
    }

    /*This API can be used if manually we need run the check on maturity date and set the expiration flag*/
    @PutMapping(value = TradeUri.ARCHIVE, params = VERSION)
    ResponseEntity<Object> archiveTrades() {
        LOGGER.info("Entry in archive Trade method");
        List<String> archivedTrades = tradeManager.updateTradeExpiration();
        Map<String, List<String>> responseMap = new HashMap<>();
        responseMap.put("ExpiredTradeIds", archivedTrades);
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}

