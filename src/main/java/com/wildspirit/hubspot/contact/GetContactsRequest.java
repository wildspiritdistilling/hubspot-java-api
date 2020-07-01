package com.wildspirit.hubspot.contact;

import java.util.ArrayList;
import java.util.List;

public final class GetContactsRequest {
    public final Long companyId;
    public final List<String> properties;
    public final PropertyMode propertyMode;
    public final FormSubmissionMode formSubmissionMode;
    public final Boolean showListMemberships;
    public final Integer count;

    public GetContactsRequest(Long companyId, List<String> properties, PropertyMode propertyMode, FormSubmissionMode formSubmissionMode, boolean showListMemberships, int count) {
        this.companyId = companyId;
        this.properties = properties;
        this.propertyMode = propertyMode;
        this.formSubmissionMode = formSubmissionMode;
        this.showListMemberships = showListMemberships;
        this.count = count;
    }

    public enum PropertyMode {
        VALUE_ONLY,
        VALUE_AND_HISTORY
    }

    public enum FormSubmissionMode {
        ALL,
        NONE,
        NEWEST,
        OLDEST
    }

    public static class Builder {
        private List<String> properties = new ArrayList<>();
        private PropertyMode propertyMode;
        private FormSubmissionMode formSubmissionMode;
        private boolean showListMemberships = false;
        private int count = 100;

        public Builder setProperties(List<String> properties) {
            this.properties = properties;
            return this;
        }

        public Builder setPropertyMode(PropertyMode propertyMode) {
            this.propertyMode = propertyMode;
            return this;
        }

        public Builder setFormSubmissionMode(FormSubmissionMode formSubmissionMode) {
            this.formSubmissionMode = formSubmissionMode;
            return this;
        }

        public Builder setShowListMemberships(boolean showListMemberships) {
            this.showListMemberships = showListMemberships;
            return this;
        }

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public GetContactsRequest build() {
            return new GetContactsRequest(null, properties, propertyMode, formSubmissionMode, showListMemberships, count);
        }
    }
}
