package dev.tobiadegbuji.jazzifiedservicegateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

public class FilterUtils {

    //Checks if id is present in request headers.
    public static String getCorrelationID(HttpHeaders reqHeaders){
        if(reqHeaders.get(CommonConstants.CORRELATION_ID) != null)
        return  (reqHeaders.get(CommonConstants.CORRELATION_ID))
                  .stream()
                  .findFirst()
                  .get();
        else
            return null;

    }

    //Adds Request Headers
    private static ServerWebExchange setRequestHeaders(ServerWebExchange exchange, String name, String value){
        return exchange.mutate().request(
                exchange.getRequest().mutate()
                .header(name, value)
                .build())
                .build();
    }


    //Used to add a correlation ID to request
    public static ServerWebExchange setCorrelationID(ServerWebExchange exchange, String correlationID){
        return FilterUtils.
                setRequestHeaders(exchange,CommonConstants.CORRELATION_ID,correlationID);
    }

}
