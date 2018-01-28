package models;

import org.mongodb.morphia.annotations.Entity;

@Entity(noClassnameStored = true)
public class UserSession extends BaseModel {
    public String userId;
    public String token;
 
    public enum Fields {userId, token}

}