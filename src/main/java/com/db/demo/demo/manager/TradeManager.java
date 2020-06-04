package com.db.demo.demo.manager;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.dto.TradeResponseDto;
import com.db.demo.demo.exception.TradeServiceException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Savitha
 */
@Component
public interface TradeManager {

    /**
     * Gets all the current trades from the trade store.
     *
     * @return
     */
    List<TradeDto> getActiveExistingTrades();

    /**
     * Gets all the current trades from the trade store.
     *
     * @return
     */
    List<TradeDto> getAllExistingTrades();

    /**
     * Creates/updates , new/existing trade for the given criteria.
     */
    long createTrades(List<TradeDto> tradeDtos);

    /**
     * Creates/updates , new/existing trade for the given criteria.
     */
    TradeResponseDto updateTrade(TradeDto tradeDto) throws TradeServiceException;

    /**
     * Checks if the trade is getting expired and updates the same and returns tradeIds which are expired.
     *
     * @return
     */
    List<String> updateTradeExpiration();

}
