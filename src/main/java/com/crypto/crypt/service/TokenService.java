package com.crypto.crypt.service;

import java.util.List;

import com.crypto.crypt.model.tiers.SessionUser;

public class TokenService extends Service {
    public TokenService() {
        super();
    }

    public boolean isValid(String token) throws Exception {
        List<SessionUser> sessions = getNgContext().findWhereArgs(SessionUser.class, "token = ?", token);
        
        if (sessions.isEmpty()) {
            return false;
        }

        SessionUser session = sessions.get(0);
         if (System.currentTimeMillis() > session.getExpiration().getTime()) {
             System.out.println("Expiration = " + session.getExpiration());
             System.out.println("Expired Token");
             return false;
         }
        return true;
    }
}
