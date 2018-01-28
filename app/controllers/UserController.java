package controllers;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dao.UserDAOImpl;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.UserAuthenticator;

public class UserController extends Controller{
	protected static boolean isUnAuthorized;
	protected static boolean notFound;
	protected static boolean isForbidden;
	
	
	private static final String getLoggedInUser() {
		final String userId = (String) ctx().args.get("userId");
		return userId;
	}

	public static Result constructApiResponse(String result) {
		ObjectNode responseObj = Json.newObject();
		responseObj.put("message", result);
		 if (isUnAuthorized) { 
			return unauthorized(responseObj);
		} else if (notFound) {
			return notFound(responseObj);
		} else if (isForbidden) {
			return forbidden(responseObj);
		} else {
			return badRequest(responseObj);
		}
	}

	private  UserDAOImpl userDAOImpl;

	@Inject
	public UserController( final UserDAOImpl userDAOImpl) {
		this.userDAOImpl = userDAOImpl;
	}

	/*
	 * 
	 */
	public  Result signUp() {
		try {
			JsonNode inputJson = null;
			inputJson = request().body().asJson();
			Logger.info(inputJson.toString());
			ObjectNode result = userDAOImpl.signUp(inputJson);
			Logger.info(result.asText());
			return ok(result);
		}catch (Exception e) {
			System.out.println(e);
			return constructApiResponse(e.getMessage());
		} 
	}
	
	/*
	 * 
	 */
	public  Result signIn() {
		try {
			JsonNode inputJson = null;
			inputJson = request().body().asJson();
			ObjectNode result = userDAOImpl.signIn(inputJson);
			return ok(result);
		}catch (Exception e) {
			return constructApiResponse(e.getMessage());
		} 
	}
	
	/*
	 * 
	 */
	@Security.Authenticated(UserAuthenticator.class)
	public  Result followUnfollow() {
		try {
			JsonNode inputJson = null;
			inputJson = request().body().asJson();
			userDAOImpl.followUser(inputJson.get("userId").asText(), getLoggedInUser(), inputJson.get("isFollow").asBoolean());
			ObjectNode responseObj = Json.newObject();
			responseObj.put("message", "Update Success");
			return ok(responseObj);
		}catch (Exception e) {
			return constructApiResponse(e.getMessage());
		} 
	}
	
	/*
	 * 
	 */
	@Security.Authenticated(UserAuthenticator.class)
	public  Result getFollowerOrFollowee(Boolean isFollower) {
		try {
			List<User> resultList = userDAOImpl.getMyFollowersOrFollowee(getLoggedInUser(), isFollower);
			ObjectMapper mapper = new ObjectMapper();
			final JsonNode result = mapper.convertValue(resultList, JsonNode.class);
			return ok(result);
		}catch (Exception e) {
			return constructApiResponse(e.getMessage());
		} 
	}
	
	
}
