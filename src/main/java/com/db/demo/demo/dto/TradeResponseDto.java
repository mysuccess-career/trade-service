package com.db.demo.demo.dto;

import com.db.demo.demo.entity.TradeStoreEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Savitha
 */

@Data
@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeResponseDto implements Serializable {
    private static final long serialVersionUID = 786676590658699144L;

    private Integer noOfTradesUpdated;
    private Integer noOfTradesCreated;
    private List<TradeStoreEntity> tradeDtos;
    private boolean isTradeMaturityExpired;
    private List<String> expiredTradeIds;

}
