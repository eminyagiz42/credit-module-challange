package com.eminyagiz.creditModule.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PayResponse {

    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private Integer payCount;
    private Boolean payAll;
    private BigDecimal unpaidAmount;
}
