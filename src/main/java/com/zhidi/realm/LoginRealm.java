package com.zhidi.realm;

import com.zhidi.dao.LoginDao;
import com.zhidi.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/14/014.
 */
public class LoginRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = String.valueOf(principalCollection.getPrimaryPrincipal());

        List<String> permissions = new ArrayList<String>();
        permissions.add("user:*");

        List<String> roles = new ArrayList<String>();
        roles.add("sysadmin");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);

        info.addRoles(roles);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String username = String.valueOf(authenticationToken.getPrincipal());
        String password = String.valueOf((char[]) authenticationToken.getCredentials());
        User user = null;
        try {
            user = new LoginDao().login(username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user == null) {
            return null;
        }
        if (!password.equals(user.getPassword())) {
            return null;
        }
        AuthenticationInfo info = new SimpleAuthenticationInfo(username, password, this.getName());
        return info;
    }
}
