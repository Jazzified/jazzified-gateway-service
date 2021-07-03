package dev.tobiadegbuji.jazzifiedservicegateway.filters;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import dev.tobiadegbuji.jazzifiedservicegateway.utils.FilterUtils;

import java.util.UUID;
import java.util.function.Supplier;

@Order(1)
@Component
@Log4j2
public class TrackingFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //Grab headers from request
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

        if(isCorrelationIdPresent(() -> requestHeaders))
            log.debug(() -> ("correlation_id found in headers: "+ FilterUtils.getCorrelationID(requestHeaders)));
        else{
            String correlationId = generateCorrelationID();
            //Decorating the SeverWebExchange
            exchange = FilterUtils.setCorrelationID(exchange,correlationId);
            log.debug(() -> "correlation_id generated in tracking filter: " + correlationId);
        }

        return chain.filter(exchange);
    }


    private boolean isCorrelationIdPresent(Supplier<HttpHeaders> requestHeaders){
       return  FilterUtils.getCorrelationID(requestHeaders.get()) != null;
    }

    private String generateCorrelationID(){
        return UUID.randomUUID().toString();
    }




}
