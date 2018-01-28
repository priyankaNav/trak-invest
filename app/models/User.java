package models;

import org.mongodb.morphia.annotations.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints;

@Entity(noClassnameStored = true)
public class User extends BaseModel {
	
    public String userId;
    
    public String fullname;
    
    @JsonIgnore
    public String password;
   
    @JsonIgnore
    @Constraints.Email
    public String email;
    
   
    public enum Fields {userId, fullname, email}

}

