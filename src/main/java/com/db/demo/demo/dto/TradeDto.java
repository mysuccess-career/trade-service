package com.db.demo.demo.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
@EqualsAndHashCode(exclude = {"createdDate", "expired", "maturity", "bookId", "counterPartyId"})
public class TradeDto implements Serializable {
    private static final long serialVersionUID = 786676590658699144L;
    @NotNull(message = "trade Id can not be null")
    private String tradeId;
    @NotNull(message = "version can not be null")
    private int version;
    private String counterPartyId;
    private String bookId;
    @NotNull(message = "maturity date can not be null")
    private Date maturity;
    private Date createdDate;
    private String expired;
}
