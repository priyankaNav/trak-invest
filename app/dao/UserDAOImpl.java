package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.mongodb.morphia.Datastore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Follow;
import models.User;
import models.UserSession;
import play.libs.Json;
import utils.MongoConnectionDB;
import utils.PasswordEncryptDecrypt;

public class UserDAOImpl {

	
	public static Datastore dbConnection = MongoConnectionDB.getDS();
	
	
   
	/**
     * 
     * @param email
     * @return
     */
	public  User findUserByEmail(@Nonnull String email) {
		if (dbConnection != null){
			return dbConnection.find(User.class).field("email").equal(email).get();
		}
		else{
			return null;
		}
		
	}
    
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public  User findUserById(String userId) {
		return dbConnection.find(User.class).field("userId").equal(userId).get();
	}
    
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public  ObjectNode generateSession(String userId) {
		UserSession session = new UserSession();
		session.token = UUID.randomUUID().toString();
		session.userId = userId;
		dbConnection.save(session);

		ObjectNode result = Json.newObject();
		result.put("userId", userId);
		result.put("token", session.token);
		return result;
	}
    
	/**
	 * 
	 * @param accessToken
	 * @return
	 */
	public static UserSession findUserSessionByToken(String accessToken) {
		return dbConnection.find(UserSession.class).field("token").equal(accessToken).get();
	}
	
	/**
	 * 
	 * @param inputData
	 * @return
	 * @throws Exception
	 */
	public  ObjectNode signUp(JsonNode inputData) throws Exception {
		User findUser = findUserByEmail(inputData.get("email").asText());
		if (findUser == null) {
			User newUser = new User();
			newUser.email = inputData.get("email").asText();
			newUser.fullname = inputData.get("fullname").asText();
			newUser.password = PasswordEncryptDecrypt.generatePasswordHash(inputData.get("password").asText());
			newUser.userId = UUID.randomUUID().toString();
			dbConnection.save(newUser);
			ObjectNode result = Json.newObject();
			result = generateSession(newUser.userId);
			return result;
		} else {
			throw new Exception("User Already Registered. Try to login");
		}
	}
    
	/**
	 * 
	 * @param inputData
	 * @return
	 * @throws Exception
	 */
	public  ObjectNode signIn(JsonNode inputData) throws Exception {
		User findUser = findUserByEmail(inputData.get("email").asText());
		if (findUser != null) {
			ObjectNode result = Json.newObject();
			if (PasswordEncryptDecrypt.isPasswordSame(inputData.get("password").asText(), findUser.password)) {
				result = generateSession(findUser.userId);
			} else {
				throw new Exception("Invalid email or password");
			}
			return result;
		} else {
			throw new Exception("User not registered. Sign Up to proceed");
		}
	}
	
	/**
	 * 
	 * @param followeeId
	 * @param followerId
	 * @return
	 */
	public  Follow alreadyFollow(String followeeId, String followerId) {
		return dbConnection.find(Follow.class).filter("followerId", followerId).filter("followeeId", followeeId)
				.get();
	}
	
	/**
	 * 
	 * @param userId
	 * @param currentUserId
	 * @param isFollow
	 * @throws Exception
	 */
	public void followUser(String userId, String currentUserId, Boolean isFollow) throws Exception {
		User findUser = findUserById(userId);
		if (findUser != null) {
			Follow followed = alreadyFollow(userId, currentUserId);
			if (isFollow) {
				if (followed == null) {
					Follow followObject = new Follow();
					followObject.followerId = currentUserId;
					followObject.followeeId = userId;
					dbConnection.save(followObject);
				}
			} else {
				if (followed != null) {
					dbConnection.delete(followed);
				} else {
					throw new Exception("You don't follow the user already!");
				}
			}
		} else {
			throw new Exception("User doesn't exists");
		}
	}
    
	/**
	 * 
	 * @param userIdList
	 * @return
	 */
	public  List<User> getUserInfo(List<String> userIdList){
		return dbConnection.find(User.class).field("userId").in(userIdList).project("_id", false).asList();
	}
	
	/**
	 * 
	 * @param userId
	 * @param isFollower
	 * @return
	 */
	public  List<User> getMyFollowersOrFollowee(String userId, Boolean isFollower){
		List<Follow> followList = new ArrayList<Follow>();
		List<String> userIds = new ArrayList<>();
		if(isFollower){
			followList = dbConnection.find(Follow.class).filter("followeeId", userId).asList();
			if(followList != null && !followList.isEmpty()){
				followList.forEach(each	->	{userIds.add(each.followerId);});
			}
		}else{
			followList = dbConnection.find(Follow.class).filter("followerId", userId).asList();
			if(followList != null && !followList.isEmpty()){
				followList.forEach(each	->	{userIds.add(each.followeeId);});
			}
		}
		List<User> usersInfo = userIds!=null && !userIds.isEmpty() ? getUserInfo(userIds) : new ArrayList<User>();
		
	return usersInfo;
	}
	
	
}
