package com.rakib.webclient.controller.CoronaReportController.service;

import com.rakib.webclient.payload.GithubRepo;
import com.rakib.webclient.payload.RepoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GitHubClient {

    Flux<GithubRepo> listGithubRepositories();

    Mono<GithubRepo> createGithubRepository(RepoRequest createRepoRequest);

    Mono<GithubRepo> getGithubRepository(String owner, String repo);

    Mono<Void> deleteGithubRepository(String owner, String repo);

    Mono<GithubRepo> editGithubRepository(String owner, String repo, RepoRequest editRepoRequest);
}
