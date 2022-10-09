package com.rakib.webclient.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class RepoRequest {
    @NotBlank
    private String name;

    private String description;

    @JsonProperty("private")
    private Boolean isPrivate;
}
