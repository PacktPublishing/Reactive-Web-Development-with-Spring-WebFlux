package com.packt.webclientwebflux.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)

public class EpisodeResponse {

    private String id;
    private String name;
    private String air_date;
    private String episode;
    private List<String> characters;
    private String url;

}
