package com.example.controllers;

import com.example.pojos.SearchResult;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import javax.swing.event.HyperlinkEvent;
import java.io.IOException;

@Controller("/search")
public class SearchController {

    @Get(value = "/{query}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<String> searchQuery(@PathVariable String query) {
        try {
            // ElasticSearch client
            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("localhost", 9200, "http"),
                            new HttpHost("localhost", 9201, "http")
                    )
            );
            // ElasticSearch server info
            MainResponse response = client.info(RequestOptions.DEFAULT);
            String clusterVersion = response.getVersion().getNumber();
            // Query result
            try {
                String json = new ObjectMapper().writeValueAsString(
                        new SearchResult(query, clusterVersion)
                );
                return HttpResponse.ok().body(json);
            } catch (JsonProcessingException e) {
                // Error mapping the query
                return HttpResponse.badRequest();
            }
        } catch (IOException e) {
            // Error contacting the ElasticSearch server
            return HttpResponse.serverError();
        }
    }

}
