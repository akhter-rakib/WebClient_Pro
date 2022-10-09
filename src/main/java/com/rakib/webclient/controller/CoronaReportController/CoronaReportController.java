package com.rakib.webclient.controller.CoronaReportController;

import com.rakib.webclient.config.AppProperties;
import com.rakib.webclient.controller.CoronaReportController.service.GitHubClient;
import com.rakib.webclient.payload.GithubRepo;
import com.rakib.webclient.payload.RepoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
public class CoronaReportController {

    private final WebClient.Builder webClientBuilder;

    private final GitHubClient gitHubClient;
    private final AppProperties appProperties;

    public CoronaReportController(WebClient.Builder webClientBuilder, GitHubClient gitHubClient, AppProperties appProperties) {
        this.webClientBuilder = webClientBuilder;
        this.gitHubClient = gitHubClient;
        this.appProperties = appProperties;
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

    @PostMapping("/repos")
    public Mono<GithubRepo> createGithubRepository(@RequestBody RepoRequest repoRequest) {
        return gitHubClient.createGithubRepository(repoRequest);
    }

    @GetMapping("/repos")
    public Flux<GithubRepo> listGithubRepositories() {
        return gitHubClient.listGithubRepositories();
    }

    @GetMapping("/repos/{repo}")
    public Mono<GithubRepo> getGithubRepository(@PathVariable String repo) {
        return gitHubClient.getGithubRepository(appProperties.getGithub().getUsername(), repo);
    }

    @DeleteMapping("/repos/{repo}")
    public Mono<Void> deleteGithubRepository(@PathVariable String repo) {
        return gitHubClient.deleteGithubRepository(appProperties.getGithub().getUsername(), repo);
    }

    @PatchMapping("/repos/{repo}")
    public Mono<GithubRepo> editGithubRepository(@PathVariable String repo, @Valid @RequestBody RepoRequest repoRequest) {
        return gitHubClient.editGithubRepository(appProperties.getGithub().getUsername(), repo, repoRequest);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(),
                ex.getResponseBodyAsString(), ex);
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
