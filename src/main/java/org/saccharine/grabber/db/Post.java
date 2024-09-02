package org.saccharine.grabber.db;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Post extends BaseEntity {
    private String id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;

    public Post(String title, String link, String description, LocalDateTime created) {
        this.id = super.getId();
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Post post = (Post) other;
        return Objects.equals(id, post.id)
                && Objects.equals(title, post.title)
                && Objects.equals(link, post.link)
                && Objects.equals(description, post.description)
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, link, description, created);
    }

    @Override
    public String toString() {
        return "Post{"
                + "id='" + id + '\''
                + ", title='" + title + '\''
                + ", link='" + link + '\''
                + ", description='" + description + '\''
                + ", created=" + created
                + '}';
    }
}