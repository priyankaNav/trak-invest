package models;

import org.mongodb.morphia.annotations.Entity;

@Entity(noClassnameStored = true)
public class Follow extends BaseModel {
    
    public String followerId;
    public String followeeId;
    

    public enum Fields {followerId, followeeId}

}