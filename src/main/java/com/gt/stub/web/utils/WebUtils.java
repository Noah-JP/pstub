package com.gt.stub.web.utils;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by noah on 2017. 5. 21..
 */
public class WebUtils {

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(query)) {
            Stream.of(StringUtils.delimitedListToStringArray(query, "&"))
                    .forEach(param -> {
                        if (!StringUtils.isEmpty(param)) {
                            int separatorIndex = param.indexOf("=");
                            if (separatorIndex != -1) {
                                String key = param.substring(0, separatorIndex).toLowerCase();
                                String value = param.length() > separatorIndex + 1 ? param.substring(separatorIndex + 1) : null;
                                queryMap.put(key, value);
                            } else {
                                queryMap.put(param, null);
                            }
                        }
                    });
        }
        return queryMap;
    }

    public static String mapToQuery(Map<String, String> queryMap) {
        StringBuilder queryAppender = new StringBuilder();
        if (Objects.nonNull(queryMap)) {
            queryMap.entrySet().stream().forEach(entry -> {
                queryAppender.append("&");
                queryAppender.append(entry.getKey().toUpperCase());
                queryAppender.append("=");
                if(Objects.nonNull(entry.getValue())){
                    queryAppender.append(entry.getValue());
                }
            });
            String result = queryAppender.toString();
            return result.substring(1, result.length());
        }
        return queryAppender.toString();
    }
}
