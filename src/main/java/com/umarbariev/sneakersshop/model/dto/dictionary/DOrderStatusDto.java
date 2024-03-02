package com.umarbariev.sneakersshop.model.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DOrderStatusDto {
    public static final String CREATED_CODE = "CREATED";
    public static final String FILLING_CODE = "FILLING";
    public static final String DELIVERING_CODE = "DELIVERING";
    public static final String DELIVERED_CODE = "DELIVERED";
    public static final String COMPLETED_CODE = "COMPLETED";
    public static final String RETURN_CODE = "RETURN";
    public static final String DECLINED_CODE = "DECLINED";

    private Long id;
    private String code;
    private String name;
    private Integer ordinal;
}
