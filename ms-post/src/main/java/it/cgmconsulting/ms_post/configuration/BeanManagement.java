package it.cgmconsulting.ms_post.configuration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.ms_post.repository.PostRepository;
import it.cgmconsulting.ms_post.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeanManagement {
    @Value("${application.security.internalToken}")
    String internalToken;
    private final PostRepository postRepository;

    @Bean("getWriters")
    @Scope("prototype")
    @CircuitBreaker(name="a-tentativi", fallbackMethod = "fallbackMethodGetWriters")
    public Map<String, String> getWriters(){

        Set<Integer> authorIds = postRepository.getAuthorIds();
        RestTemplate restTemplate = new RestTemplate();
        String url= Consts.GATEWAY+"/"+Consts.MS_AUTH+"/v99/role";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);
        HttpEntity<Set<Integer>> httpEntity = new HttpEntity<>(authorIds, headers);
        try{
            ResponseEntity<?> r = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            if(r.getStatusCode().equals(HttpStatus.OK))
                return (Map<String, String>) r.getBody();
        } catch (RestClientException e){
            log.error("Method GetWriters: "+e.getMessage());
            //return null;
            throw e;
        }
        return null;
    }

    public Map<String, String> fallbackMethodGetWriters(Exception e){
        log.error(e.getMessage());
        return new HashMap<String, String>();
    }

}

