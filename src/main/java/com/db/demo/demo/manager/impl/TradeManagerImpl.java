package com.db.demo.demo.manager.impl;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.dto.TradeResponseDto;
import com.db.demo.demo.entity.TradeStoreEntity;
import com.db.demo.demo.exception.TradeServiceException;
import com.db.demo.demo.manager.TradeManager;
import com.db.demo.demo.repository.TradeRepository;
import com.db.demo.demo.util.TradeDataTransformerUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

/**
 * @author Savitha
 */

@Service
public class TradeManagerImpl implements TradeManager {

    @Autowired
    TradeRepository tradeRepository;

    @Override
    public List<TradeDto> getActiveExistingTrades() {
        List<TradeStoreEntity> tradeEntities = tradeRepository.getAllActiveExistingTrades();
        return TradeDataTransformerUtil.getTradeDtos(tradeEntities);
    }


    @Override
    public List<TradeDto> getAllExistingTrades() {
        List<TradeStoreEntity> tradeEntities = tradeRepository.getAllExistingTrades();
        System.out.println("tradeStoreEntities" + tradeEntities);

        return TradeDataTransformerUtil.getTradeDtos(tradeEntities);
    }

    @Override
    public long createTrades(List<TradeDto> tradeDtos) {
        long noOfTradeStoreEntitiesSaved = 0;
        List<TradeStoreEntity> tradeStoreEntities = new ArrayList<>();
        List<TradeStoreEntity> tradeStoreEntitiesSaved = null;
        if(!CollectionUtils.isEmpty(tradeDtos)) {
            tradeDtos.forEach(tradeDto -> {
                TradeStoreEntity tradeStoreEntity = new TradeStoreEntity();
                BeanUtils.copyProperties(tradeDto, tradeStoreEntity);
                tradeStoreEntities.add(tradeStoreEntity);
            });
            tradeStoreEntitiesSaved = tradeRepository.saveAll(tradeStoreEntities);
        }
        if(!CollectionUtils.isEmpty(tradeStoreEntitiesSaved)) {
            noOfTradeStoreEntitiesSaved = tradeStoreEntitiesSaved.size();
        }
        return noOfTradeStoreEntitiesSaved;
    }

    @Override
    public TradeResponseDto updateTrade(TradeDto tradeDtoInput) throws TradeServiceException {
        TradeResponseDto tradeResponseDto = new TradeResponseDto();
        TradeStoreEntity tradeStoreEntity;
        Date maturity = tradeDtoInput.getMaturity();
        int noOfTradesCreated = 0;
        int noOfTradesUpdated = 0;

        boolean isMaturityDateValid = maturity.compareTo(Calendar.getInstance().getTime()) == 0 || maturity.compareTo(Calendar.getInstance().getTime()) > 0;
        if(isMaturityDateValid) {
            List<TradeStoreEntity> allExistingTradeEntities = tradeRepository.getAllActiveExistingTrades();// TODO We should fetch the latest matching trade entity instead of all and avoid below filter as well
            if(!CollectionUtils.isEmpty(allExistingTradeEntities)) {
                Map<Integer, TradeStoreEntity> tradeVersionEntityMap = allExistingTradeEntities.stream().filter(existingTradeStoreEntity -> existingTradeStoreEntity.getTradeId().equalsIgnoreCase(tradeDtoInput.getTradeId())).collect(Collectors
                        .toMap(entity -> entity.getVersion(),
                                entity -> entity));
                if(!CollectionUtils.isEmpty(tradeVersionEntityMap)) {
                    int latestTradeVersion = max(tradeVersionEntityMap.keySet());
                    if(tradeDtoInput.getVersion() < latestTradeVersion) {
                        throw new TradeServiceException("Trade version can not be lesser than the existing trade");
                    } else {
                        tradeStoreEntity = new TradeStoreEntity();
                        if(latestTradeVersion == tradeDtoInput.getVersion()) {
                            tradeStoreEntity.setId(tradeVersionEntityMap.get(latestTradeVersion).getId());
                            noOfTradesUpdated++;
                        } else {
                            noOfTradesCreated++;
                        }
                        BeanUtils.copyProperties(tradeDtoInput, tradeStoreEntity);
                    }
                } else {
                    noOfTradesCreated++;
                    tradeStoreEntity = new TradeStoreEntity();
                    BeanUtils.copyProperties(tradeDtoInput, tradeStoreEntity);
                }
                List<TradeStoreEntity> tradeStoreEntitiesSaved = tradeRepository.saveAll(singletonList(tradeStoreEntity));
                if(!CollectionUtils.isEmpty(tradeStoreEntitiesSaved)) {
                    tradeResponseDto.setTradeDtos(tradeStoreEntitiesSaved);
                }
            }
        } else {
            tradeResponseDto.setTradeMaturityExpired(true);
        }
        tradeResponseDto.setNoOfTradesCreated(noOfTradesCreated);
        tradeResponseDto.setNoOfTradesUpdated(noOfTradesUpdated);
        return tradeResponseDto;
    }

    @Override
    public List<String> updateTradeExpiration() {
        List<TradeStoreEntity> expiredTradeEntities = new ArrayList<>();
        List<TradeStoreEntity> savedExpiredTradeEntities = new ArrayList<>();
        List<TradeStoreEntity> activeTradeObjectList = tradeRepository.getAllActiveExistingTrades();
        if(!CollectionUtils.isEmpty(activeTradeObjectList)) {
            activeTradeObjectList.forEach(activeTradeEntity -> {
                Calendar currentDate = Calendar.getInstance();
                if(activeTradeEntity.getMaturity().compareTo(currentDate.getTime()) < 0) {
                    activeTradeEntity.setExpired("Y");
                    expiredTradeEntities.add(activeTradeEntity);
                }
            });
            savedExpiredTradeEntities = tradeRepository.saveAll(expiredTradeEntities);
        }
        return savedExpiredTradeEntities.stream().map(tradeStoreEntity -> tradeStoreEntity.getTradeId()).collect(Collectors.toList());
    }
}
