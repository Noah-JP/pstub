package com.gt.stub.web.holder;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by nttd-gy-chie on 2017/05/24.
 */
@Component
@ConfigurationProperties("card")
@Data
public class CardProperties {

    private Point point;
    private String password;

    @Data
    public static class Point {
        @NotEmpty
        private String amount;
        @NotEmpty
        private String expirationDate;
        @NotEmpty
        private String expirationAmount;
    }
}
