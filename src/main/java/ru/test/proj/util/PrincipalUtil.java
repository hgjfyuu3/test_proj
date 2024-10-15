package ru.test.proj.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.test.proj.model.UsersEntity;

import java.security.Principal;

public class PrincipalUtil {

    private PrincipalUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Long getIdFromPrincipal(Principal principal) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        UsersEntity usersEntity = (UsersEntity) usernamePasswordAuthenticationToken.getPrincipal();
        return usersEntity.getId();
    }

}