package com.rakib.webclient.controller.CoronaReportController.service;

import com.rakib.webclient.config.AppProperties;
import com.rakib.webclient.payload.GithubRepo;
import com.rakib.webclient.payload.RepoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class GitHubClientImpl implements GitHubClient {
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private static final String GITHUB_V3_MIME_TYPE = "application/vnd.github.v3+json";
    private static final String USER_AGENT = "Spring 5 WebClient";
    private final WebClient webClient;


    public GitHubClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Autowired
    public GitHubClientImpl(AppProperties appProperties) {
        this.webClient = WebClient.builder()
                .baseUrl(GITHUB_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, GITHUB_V3_MIME_TYPE)
                .defaultHeader(HttpHeaders.USER_AGENT, USER_AGENT)
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(appProperties.getGithub().getUsername(),
                                appProperties.getGithub().getToken()))
                .filter(logRequest())
                .build();
    }


    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    @Override
    public Flux<GithubRepo> listGithubRepositories() {
        return webClient.get().uri("/user/repos?sort={sortField}&direction={sortDirection}",
                "updated", "desc").retrieve().bodyToFlux(GithubRepo.class);
    }

    @Override
    public Mono<GithubRepo> createGithubRepository(RepoRequest createRepoRequest) {
        return webClient.post()
                .uri("/user/repos")
                .body(Mono.just(createRepoRequest), RepoRequest.class)
                .retrieve()
                .bodyToMono(GithubRepo.class);
    }

    @Override
    public Mono<GithubRepo> getGithubRepository(String owner, String repo) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(GithubRepo.class);
    }

    @Override
    public Mono<Void> deleteGithubRepository(String owner, String repo) {
        return webClient.delete()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<GithubRepo> editGithubRepository(String owner, String repo, RepoRequest editRepoRequest) {
        return webClient.patch()
                .uri("/repos/{owner}/{repo}", owner, repo)
                .body(Mono.just(editRepoRequest), RepoRequest.class)
                .retrieve()
                .bodyToMono(GithubRepo.class);
    }
}
