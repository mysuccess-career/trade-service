package com.db.demo.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Savitha
 */

@Builder
@Data
public class ErrorTemplate implements Serializable {

    private static final long serialVersionUID = -7339791043828169476L;
    String errorMessage;
    Integer errorCode;
}
