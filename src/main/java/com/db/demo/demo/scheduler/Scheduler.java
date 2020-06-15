package com.db.demo.demo.scheduler;

import com.db.demo.demo.manager.TradeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.management.timer.Timer;
import java.text.ParseException;
import java.util.List;

/**
 * @author Savitha
 */

@Component
public class Scheduler {

    Logger LOGGER = LogManager.getLogger(Scheduler.class);
    @Autowired
    TradeManager tradeManager;

    @Scheduled(initialDelay = Timer.ONE_MINUTE * 1, fixedDelay = Timer.ONE_MINUTE * 10)
    public void updateMaturityFlagScheduler() throws ParseException {
        LOGGER.info("Running scheduler service");
        List<String> tradesUpdated = tradeManager.updateTradeExpiration();
        LOGGER.info("trades which are expired:{}", tradesUpdated);
        LOGGER.info("Exit from scheduler service");
    }

}
