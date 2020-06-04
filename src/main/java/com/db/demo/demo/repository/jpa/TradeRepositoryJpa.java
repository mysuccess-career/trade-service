package com.db.demo.demo.repository.jpa;

import com.db.demo.demo.entity.TradeStoreEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Savitha
 */

public interface TradeRepositoryJpa extends JpaRepository<TradeStoreEntity, String> {

    @Query(value = "SELECT TRADE_ID, MATURITY FROM trade_store where EXPIRED = :isExpired", nativeQuery = true)
    List<Object[]> findActiveTrades(@Param("isExpired") String isExpires);

    @Query(value = "UPDATE trade_store set EXPIRED='Y' where trade_id in (:tradeIds)", nativeQuery = true)
    void updateTradesForExpiry(@Param("tradeIds") List<String> tradeIds);

    @Query(value = "SELECT * FROM trade_store where EXPIRED = 'N'", nativeQuery = true)
    List<TradeStoreEntity> findActiveTradesData();
}




