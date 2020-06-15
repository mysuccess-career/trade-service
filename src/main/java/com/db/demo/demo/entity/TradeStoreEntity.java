package com.db.demo.demo.entity;

import lombok.Data;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @author Savitha
 */

@Entity
@Data
@Table(name = "trade_store")
public class TradeStoreEntity implements Serializable {
    private static final long serialVersionUID = 21529532332293426L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int id;

    @Column(name = "TRADE_ID")
    private String tradeId;

    @Column(name = "VERSION")
    private int version;

    @Column(name = "COUNTER_PARTY_ID")
    private String counterPartyId;

    @Column(name = "BOOK_ID")
    private String bookId;

    @Column(name = "MATURITY")
    private Date maturity;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "EXPIRED")
    private String expired;
}