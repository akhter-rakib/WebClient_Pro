package com.rakib.webclient.controller.CoronaReportController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class CoronaReportController {

    private final WebClient.Builder webClientBuilder;

    public CoronaReportController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/corona-result/{country}")
    public ResponseEntity<?> coronaReport(@PathVariable String country) {
        Object obj = webClientBuilder.build().get()
                .uri("https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total?country=" + country)
                .header("X-RapidAPI-Key", "aa22148a0dmsh478dd26a8cddb4cp14afe6jsn3442103f5b9c")
                .header("X-RapidAPI-Host", "covid-19-coronavirus-statistics.p.rapidapi.com")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok().body(obj);
    }
}
