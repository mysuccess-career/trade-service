package com.db.demo.demo.scheduler;

import com.db.demo.demo.manager.TradeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.management.timer.Timer;

@Component
public class Scheduler {

    @Autowired
    TradeManager tradeManager;

    @Scheduled(initialDelay = Timer.ONE_SECOND*10, fixedDelay = Timer.ONE_MINUTE*30)
    public void updateMaturityFlagScheduler(){
        tradeManager.updateTradeExpiration();

    }

}
