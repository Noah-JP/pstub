package com.gt.stub.web.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by noah on 2017. 5. 21..
 */
public class FormatUtils {
    public static final DateTimeFormatter DATE_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter DATE_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DecimalFormat DECIMAL_NO_Fraction_Digits = new DecimalFormat("#");


    public static String dateTo14digits(LocalDateTime dateTime){
        return DATE_YYYYMMDDHHMMSS.format(dateTime);
    }

    public static String dateTo8digits(LocalDateTime dateTime){
        return DATE_YYYYMMDD.format(dateTime);
    }

    public static String decimalToNoFraction(BigDecimal target){
        return DECIMAL_NO_Fraction_Digits.format(target);
    }
}
