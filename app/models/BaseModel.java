package models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * BaseModel class for all the Models
 */

public class BaseModel {
	

	@JsonIgnore @Id 
    public ObjectId id;
    
    @JsonIgnore
    public long createdAt = currentEpoch();
    
    @JsonIgnore
    public long updatedAt = currentEpoch();
   
    
    public static long currentEpoch(){
        return System.currentTimeMillis()/1000;
    }
}
