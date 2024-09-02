package org.saccharine.grabber.db;

import java.util.UUID;

public abstract class BaseEntity {
    private final String id = UUID.randomUUID().toString().trim().replaceAll("-", "");

    public String getId() {
        return id;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        BaseEntity otherEntity = (BaseEntity) other;
        return !isNew() && id.equals(otherEntity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}