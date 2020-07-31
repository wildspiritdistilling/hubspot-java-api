package com.wildspirit.hubspot.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class Paging {

    public final Next next;

    public Paging(@JsonProperty("next") Next next) {
        this.next = next;
    }

    public static final class Next {
        public final String link;
        public final String after;

        public Next(@JsonProperty("link") String link, @JsonProperty("after") String after) {
            this.link = link;
            this.after = after;
        }
    }
}
