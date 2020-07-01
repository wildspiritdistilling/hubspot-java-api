package com.wildspirit.hubspot.associations;

public final class AssociateRequest {
    public final Long fromObjectId;
    public final Long toObjectId;
    public final String category;
    public final int definitionId;

    public AssociateRequest(Long fromObjectId, Long toObjectId, String category, int definitionId) {
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

        public AssociateRequest build() {
            return new AssociateRequest(fromObjectId, toObjectId, category.name(), definitionId.id);
        }
    }
}
