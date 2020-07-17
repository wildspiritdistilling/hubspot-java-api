package com.wildspirit.hubspot.common;

import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.ResponseBody;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CollectionResponseIterator<R, T extends CollectionResponse<R>> implements Iterator<R> {

    private final UrlBuilder urlBuilder;
    private final Function<String, T> requestMaker;
    private final AbstractApi api;
    private final Class<T> responseType;
    private CollectionResponse<R> lastResponse;
    private Iterator<R> resultIterator;

    public CollectionResponseIterator(UrlBuilder urlBuilder, Function<String, T> requestMaker, AbstractApi api, Class<T> responseType) {
        this.urlBuilder = urlBuilder;
        this.requestMaker = requestMaker;
        this.api = api;
        this.responseType = responseType;
        this.lastResponse = null;
        this.resultIterator = null;
    }

    public static <R, T extends CollectionResponse<R>> CollectionResponseIterator<R, T> httpGet(UrlBuilder urlBuilder, AbstractApi api, Class<T> responseType) {
        return new CollectionResponseIterator<>(urlBuilder, s -> api.httpGet(urlBuilder.toString(), responseType), api, responseType);
    }

    public static <R, T extends CollectionResponse<R>> CollectionResponseIterator<R, T> httpPost(UrlBuilder urlBuilder, Object body, AbstractApi api, Class<T> responseType) {
        return new CollectionResponseIterator<>(urlBuilder, s -> api.httpPost(urlBuilder.toString(), body, responseType), api, responseType);
    }

    @Override
    public boolean hasNext() {
        // First request
        if (lastResponse == null) {
            lastResponse = makeRequest(null);
            resultIterator = lastResponse.results.iterator();
            return resultIterator.hasNext();
        }
        // Iterate through pages
        if (resultIterator.hasNext()) {
            return true;
        } else {
            if (lastResponse.paging != null && lastResponse.paging.next != null) {
                lastResponse = makeRequest(lastResponse.paging);
                resultIterator = lastResponse.results.iterator();
                return resultIterator.hasNext();
            } else {
                return false;
            }
        }
    }

    @Override
    public R next() {
        return resultIterator.next();
    }

    public Stream<R> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED | Spliterator.NONNULL), false);
    }

    private CollectionResponse<R> makeRequest(Paging paging) {
        UrlBuilder url = this.urlBuilder;
        if (paging != null) {
            url = urlBuilder.addParameter("after", paging.next.after);
        }
        return api.httpGet(url.toString(), responseType);
    }
}
