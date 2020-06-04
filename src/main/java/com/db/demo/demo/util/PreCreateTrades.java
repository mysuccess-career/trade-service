package com.db.demo.demo.util;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.manager.TradeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class PreCreateTrades {

    private static final Logger LOGGER = LogManager.getLogger(PreCreateTrades.class);

    @Autowired
    TradeManager tradeManager;

    @PostConstruct
    private void createTrades() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        TradeDto tradeDto = new TradeDto();
        Date dateTrade = formatter.parse("20/05/2020");
        tradeDto.setTradeId("T1");
        tradeDto.setVersion(1);
        tradeDto.setCounterPartyId("CP-1");
        tradeDto.setBookId("B1");
        tradeDto.setMaturity(dateTrade);
        tradeDto.setCreatedDate(new Date());
        tradeDto.setExpired("N");

        TradeDto tradeDto1 = new TradeDto();
        Date dateTrade1 = formatter.parse("20/05/2021");
        tradeDto1.setTradeId("T2");
        tradeDto1.setVersion(2);
        tradeDto1.setCounterPartyId("CP-2");
        tradeDto1.setBookId("B1");
        tradeDto1.setMaturity(dateTrade1);
        tradeDto1.setCreatedDate(new Date());
        tradeDto1.setExpired("N");

        TradeDto tradeDto2 = new TradeDto();
        Date createdDateTrade2 = formatter.parse("14/03/2015");

        tradeDto2.setTradeId("T2");
        tradeDto2.setVersion(1);
        tradeDto2.setCounterPartyId("CP-1");
        tradeDto2.setBookId("B1");
        tradeDto2.setMaturity(dateTrade1);
        tradeDto2.setCreatedDate(createdDateTrade2);
        tradeDto2.setExpired("N");

        TradeDto tradeDto3 = new TradeDto();
        Date dateTrade3 = formatter.parse("20/05/2014");
        tradeDto3.setTradeId("T3");
        tradeDto3.setVersion(3);
        tradeDto3.setCounterPartyId("CP-3");
        tradeDto3.setBookId("B1");
        tradeDto3.setMaturity(dateTrade3);
        tradeDto3.setCreatedDate(new Date());
        tradeDto3.setExpired("Y");

        List<TradeDto> tradeDtos = List.of(tradeDto, tradeDto1, tradeDto2, tradeDto3);
        long numberOfTradesCreated = tradeManager.createTrades(tradeDtos);
        LOGGER.info("No of Pre-created Trades:{}", numberOfTradesCreated);
    }

}
