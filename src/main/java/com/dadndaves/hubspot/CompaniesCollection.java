package com.dadndaves.hubspot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CompaniesCollection extends CollectionResponse {
    @JsonProperty("companies")
    public final List<Company> companies;

    @JsonCreator
    public CompaniesCollection(
            @JsonProperty("hasMore") boolean hasMore,
            @JsonProperty("offset") String offset,
            @JsonProperty("companies") List<Company> companies) {
        super(hasMore, offset);
        this.companies = companies;
    }
}
