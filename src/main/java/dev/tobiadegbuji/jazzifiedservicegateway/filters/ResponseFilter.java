package dev.tobiadegbuji.jazzifiedservicegateway.filters;

import dev.tobiadegbuji.jazzifiedservicegateway.utils.CommonConstants;
import dev.tobiadegbuji.jazzifiedservicegateway.utils.FilterUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
@Log4j2
public class ResponseFilter {


    @Bean
    public GlobalFilter postGlobalFilter(){
        return ((exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlation_id = FilterUtils.getCorrelationID(requestHeaders);
                log.debug(() -> "Adding correlation_id to the outbound headers: " + correlation_id);
                exchange.getResponse().getHeaders().add(CommonConstants.CORRELATION_ID, correlation_id);
                log.debug(() -> "Client transaction complete for resource: " + exchange.getRequest().getURI());
            }));
        });
    }


}

