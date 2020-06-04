package com.db.demo.demo.util;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.entity.TradeStoreEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class TradeDataTransformerUtil {

    public static List<TradeDto> getTradeDtos(List<TradeStoreEntity> tradeEntities) {
        List<TradeDto> tradeDTOs = new ArrayList<>();
        if(!CollectionUtils.isEmpty(tradeEntities)) {
            tradeEntities.forEach(tradeStoreEntity -> {
                TradeDto tradeDto = TradeDto.builder().tradeId(tradeStoreEntity.getTradeId()).maturity(tradeStoreEntity.getMaturity()).
                        bookId(tradeStoreEntity.getBookId()).counterPartyId(tradeStoreEntity.getCounterPartyId()).
                        version(tradeStoreEntity.getVersion()).createdDate(tradeStoreEntity.getCreatedDate()).expired(tradeStoreEntity.getExpired()).build();
                tradeDTOs.add(tradeDto);
            });
        }
        return tradeDTOs;
    }
}
