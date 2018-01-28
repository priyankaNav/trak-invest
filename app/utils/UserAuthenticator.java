package utils;

import dao.UserDAOImpl;
import models.UserSession;
import play.Logger;
import play.mvc.Security;
import play.mvc.Http.Context;


public class UserAuthenticator extends Security.Authenticator {
	@Override
	public String getUsername(Context ctx) {
		try {
			String accessToken = ctx.request().getHeader("token");
			if (accessToken == null) {
				return null;
			}
			String userId;
			UserSession uToken = UserDAOImpl.findUserSessionByToken(accessToken);
			Logger.info(String.format("================ Access-Token value is :: %s =================", accessToken));
			if (uToken == null) {
				return null;
			}
			userId = uToken.userId;
			ctx.args.put("userId", userId);
			return userId;
		} catch (Exception e) {
			Logger.error("Exception :: " + e.getMessage());
			return null;
		}
	}

}
