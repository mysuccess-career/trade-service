package com.db.demo.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


/**
 * @author Savitha
 */

@Builder
@Data
public class ResponseTemplate {
    private Object data;
    private List<ErrorTemplate> errors;
}
