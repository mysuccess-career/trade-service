package com.db.demo.demo.manager.impl;

import com.db.demo.demo.dto.TradeDto;
import com.db.demo.demo.dto.TradeResponseDto;
import com.db.demo.demo.entity.TradeStoreEntity;
import com.db.demo.demo.exception.TradeServiceException;
import com.db.demo.demo.manager.TradeManager;
import com.db.demo.demo.repository.TradeRepository;
import com.db.demo.demo.util.TradeDataTransformerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.db.demo.demo.util.TradeConstants.DATE_FORMAT;
import static java.util.Collections.*;

/**
 * @author Savitha
 */

@Service
public class TradeManagerImpl implements TradeManager {

    private static final Logger LOGGER = LogManager.getLogger(TradeManagerImpl.class);
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
    public TradeResponseDto updateTrade(List<TradeDto> tradeDtoInputs) throws ParseException, TradeServiceException {
        TradeResponseDto tradeResponseDto = new TradeResponseDto();
        List<String> expiredTradeIds = new ArrayList<>();
        int noOfTradesCreated = 0;
        int noOfTradesUpdated = 0;
        List<TradeStoreEntity> tradeStoreEntityToBeSaved =  new ArrayList<>();
        for(TradeDto tradeDtoInput: tradeDtoInputs) {
            TradeStoreEntity tradeStoreEntity = null;
            Date maturity = tradeDtoInput.getMaturity();
            boolean isMaturityDateValid = isMaturityDateValid(maturity);
            /**Below line of code considered with time*/
//        boolean isMaturityDateValid = maturity.compareTo(Calendar.getInstance().getTime()) == 0 || maturity.compareTo(Calendar.getInstance().getTime()) > 0;
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
                    tradeStoreEntityToBeSaved.add(tradeStoreEntity);
                    List<TradeStoreEntity> tradeStoreEntitiesSaved = tradeRepository.saveAll(tradeStoreEntityToBeSaved);
                    if(!CollectionUtils.isEmpty(tradeStoreEntitiesSaved)) {
                        tradeResponseDto.setTradeDtos(tradeStoreEntitiesSaved);
                    }
                }
            } else {
                LOGGER.error("Invalid maturity date for the trade id:{}", tradeDtoInput.getTradeId());
                expiredTradeIds.add(tradeDtoInput.getTradeId());
                tradeResponseDto.setTradeMaturityExpired(true);
            }
        }
        tradeResponseDto.setNoOfTradesCreated(noOfTradesCreated);
        tradeResponseDto.setNoOfTradesUpdated(noOfTradesUpdated);
        tradeResponseDto.setExpiredTradeIds(expiredTradeIds);
        return tradeResponseDto;
    }

    /**
     * checks if the given maturity date current date or future date
     *
     * @param maturity
     * @return returns true if given maturity date is current_date or future date otherwise returns false.
     * @throws ParseException
     */
    private boolean isMaturityDateValid(Date maturity) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String maturityDate = sdf.format(maturity);
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        Date date1 = sdf.parse(maturityDate);
        Date date2 = sdf.parse(currentDate);
        return date1.equals(date2) || date1.after(date2);
    }

    @Override
    public List<String> updateTradeExpiration() throws ParseException {
        List<TradeStoreEntity> expiredTradeEntities = new ArrayList<>();
        List<TradeStoreEntity> savedExpiredTradeEntities = new ArrayList<>();
        List<TradeStoreEntity> activeTradeObjectList = tradeRepository.getAllActiveExistingTrades();
        if(!CollectionUtils.isEmpty(activeTradeObjectList)) {
            for(TradeStoreEntity activeTradeEntity : activeTradeObjectList) {
                if(!isMaturityDateValid(activeTradeEntity.getMaturity())) {
                    activeTradeEntity.setExpired("Y");
                    expiredTradeEntities.add(activeTradeEntity);
                }
            }
            savedExpiredTradeEntities = tradeRepository.saveAll(expiredTradeEntities);
        }
        return savedExpiredTradeEntities.stream().map(tradeStoreEntity -> tradeStoreEntity.getTradeId()).collect(Collectors.toList());
    }
}
