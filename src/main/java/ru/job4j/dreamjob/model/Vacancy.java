package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vacancy {
    private int id;

    private String title;

    private String description;

    private LocalDateTime creationTime = LocalDateTime.now();

    public Vacancy(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Vacancy() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vacancy vacancy)) {
            return false;
        }
        return getId() == vacancy.getId() && Objects.equals(getTitle(), vacancy.getTitle()) && Objects.equals(getDescription(), vacancy.getDescription()) && Objects.equals(getCreationTime(), vacancy.getCreationTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getCreationTime());
    }
}
