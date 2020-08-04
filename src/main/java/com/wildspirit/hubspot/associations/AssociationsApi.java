package com.wildspirit.hubspot.associations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wildspirit.hubspot.HubSpotException;
import com.wildspirit.hubspot.common.*;
import com.wildspirit.hubspot.companies.Company;
import io.mikael.urlbuilder.UrlBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public final class AssociationsApi extends AbstractApi {

    public AssociationsApi(OkHttpClient http, String apiKey, ObjectMapper mapper) {
        super(http, apiKey, mapper);
    }

    public void associate(CreateAssociationRequest req) {
        httpPut(UrlBuilder.fromString("https://api.hubapi.com/crm-associations/v1/associations"), req, null);
    }

    public Stream<Association> find(FindAssociationsRequest req) {
        String collectionName = getCollectionName(req.parentObjectType);
        String childName = getType(req.toObjectType);
        UrlBuilder url = UrlBuilder.fromString(String.format("https://api.hubapi.com/crm/v3/objects/%s/%s/associations/%s", collectionName, req.parentId, childName));
        return CollectionResponseIterator.httpGet(url, this, FindAssociationsResponse.class).stream();
    }

    public static final class CreateAssociationRequest {
        public final Long fromObjectId;
        public final Long toObjectId;
        public final String category;
        public final int definitionId;

        public CreateAssociationRequest(Long fromObjectId, Long toObjectId, String category, int definitionId) {
            this.fromObjectId = fromObjectId;
            this.toObjectId = toObjectId;
            this.category = category;
            this.definitionId = definitionId;
        }

        public enum Category {
            HUBSPOT_DEFINED
        }

        public enum AssociationDefinition {
            CONTACT_TO_COMPANY(1);

            public final int id;

            AssociationDefinition(int id) {
                this.id = id;
            }
        }

        public static final class Builder {
            private Long fromObjectId;
            private Long toObjectId;
            private Category category = Category.HUBSPOT_DEFINED;
            private AssociationDefinition definitionId;

            public Builder definition(AssociationDefinition definition) {
                this.definitionId = definition;
                return this;
            }

            public Builder objects(Long from, Long to) {
                fromObjectId = from;
                toObjectId = to;
                return this;
            }

            public CreateAssociationRequest build() {
                return new CreateAssociationRequest(fromObjectId, toObjectId, category.name(), definitionId.id);
            }
        }
    }

    public static class FindAssociationsRequest {
        public final long parentId;
        public final Class<?> parentObjectType;
        public final Class<?> toObjectType;

        public FindAssociationsRequest(long parentId, Class<?> parentObjectType, Class<?> toObjectType) {
            this.parentId = parentId;
            this.parentObjectType = parentObjectType;
            this.toObjectType = toObjectType;
        }
    }

    public static class FindAssociationsResponse extends CollectionResponse<Association> {
        public FindAssociationsResponse(@JsonProperty("results") List<Association> results, @JsonProperty("paging") Paging paging) {
            super(results, paging);
        }
    }

    private String getCollectionName(Class<?> aClass) {
        final HubSpotType annotation = aClass.getAnnotation(HubSpotType.class);
        if (annotation == null) {
            throw new RuntimeException(aClass.getName() + " is not annotated with " + HubSpotType.class.getSimpleName());
        }
        return annotation.collectionName();
    }

    private String getType(Class<?> aClass) {
        final HubSpotType annotation = aClass.getAnnotation(HubSpotType.class);
        if (annotation == null) {
            throw new RuntimeException(aClass.getName() + " is not annotated with " + HubSpotType.class.getSimpleName());
        }
        return annotation.type();
    }
}
