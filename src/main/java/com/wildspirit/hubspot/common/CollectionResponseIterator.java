package com.wildspirit.hubspot.common;

import com.wildspirit.hubspot.companies.CompanyApi;
import io.mikael.urlbuilder.UrlBuilder;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CollectionResponseIterator<ITEM, REQ, RESP extends CollectionResponse<ITEM>> implements Iterator<ITEM> {

    private final UrlBuilder urlBuilder;
    private final Function<UrlBuilder, RESP> requestMaker;
    private CollectionResponse<ITEM> lastResponse;
    private Iterator<ITEM> resultIterator;

    public CollectionResponseIterator(UrlBuilder urlBuilder, Function<UrlBuilder, RESP> requestMaker) {
        this.urlBuilder = urlBuilder;
        this.requestMaker = requestMaker;
        this.lastResponse = null;
        this.resultIterator = null;
    }

    public static <ITEM, REQ, RESP extends CollectionResponse<ITEM>> CollectionResponseIterator<ITEM, REQ, RESP> httpGet(UrlBuilder urlBuilder, AbstractApi api, Class<RESP> responseType) {
        return new CollectionResponseIterator<>(urlBuilder, s -> api.httpGet(urlBuilder, responseType));
    }

    public static <ITEM, REQ, RESP extends CollectionResponse<ITEM>> CollectionResponseIterator<ITEM, REQ, RESP> httpPost(UrlBuilder urlBuilder, Object req, AbstractApi api, Class<RESP> responseType) {
        return new CollectionResponseIterator<>(urlBuilder, s -> api.httpPost(urlBuilder, req, responseType));
    }

    public static <ITEM, REQ, RESP extends CollectionResponse<ITEM>> CollectionResponseIterator httpPost(UrlBuilder urlBuilder, CompanyApi.SearchCompaniesRequest req, RequestWrapper<CompanyApi.SearchCompaniesRequest, CompanyApi.SearchCompaniesResponse> wrapper, AbstractApi api, Class<CompanyApi.SearchCompaniesResponse> responseType) {
        return new CollectionResponseIterator(urlBuilder, s -> {
            if (wrapper.lastResponse == null) {
                final CompanyApi.SearchCompaniesResponse resp = api.httpPost(urlBuilder, req, responseType);
                wrapper.setLastResponse(resp);
                return resp;
            } else {
                final CompanyApi.SearchCompaniesResponse resp = api.httpPost(urlBuilder, wrapper.wrapRequest(req, wrapper.lastResponse), responseType);
                wrapper.setLastResponse(resp);
                return resp;
            }
        });
    }

    @Override
    public boolean hasNext() {
        // First request
        if (lastResponse == null) {
            lastResponse = this.requestMaker.apply(urlBuilder);
            resultIterator = lastResponse.results.iterator();
            return resultIterator.hasNext();
        }
        // Iterate through pages
        if (resultIterator.hasNext()) {
            return true;
        } else {
            if (lastResponse.paging != null && lastResponse.paging.next != null) {
                lastResponse = this.requestMaker.apply(page(urlBuilder, lastResponse.paging));
                resultIterator = lastResponse.results.iterator();
                return resultIterator.hasNext();
            } else {
                return false;
            }
        }
    }

    @Override
    public ITEM next() {
        return resultIterator.next();
    }

    public Stream<ITEM> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED | Spliterator.NONNULL), false);
    }

    protected UrlBuilder page(UrlBuilder url, Paging paging) {
        if (paging != null) {
            url = urlBuilder.addParameter("after", paging.next.after);
        }
        return url;
    }

    public abstract static class RequestWrapper<M, T> {
        private T lastResponse;
        public void setLastResponse(T lastResponse) {
            this.lastResponse = lastResponse;
        }
        public abstract M wrapRequest(M req, T resp);
    }
}
