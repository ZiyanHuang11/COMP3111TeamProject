package comp3111.examsystem.entity;

import java.util.UUID;

/**
 * Abstract base class for all entities in the exam system.
 */
public abstract class Entity {
    private String id; // Unique identifier for the entity

    public Entity() {
        // Generate a unique ID for each instance
        this.id = UUID.randomUUID().toString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
