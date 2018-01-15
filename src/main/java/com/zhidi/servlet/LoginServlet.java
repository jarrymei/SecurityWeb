package com.zhidi.servlet;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.env.WebEnvironment;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2018/1/14/014.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final transient Logger log = Logger.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Boolean rememberMe = Boolean.valueOf(req.getParameter("rememberMe"));

        WebEnvironment webEnvironment = WebUtils.getRequiredWebEnvironment(req.getServletContext());
        SecurityManager securityManager = webEnvironment.getSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(rememberMe);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException un) {
                log.info("用户不存在");
                return;
            }catch ( IncorrectCredentialsException ice ) {
                //password didn't match, try again?
                log.info("密码错误");
                return;
            } catch ( LockedAccountException lae ) {
                //account for that username is locked - can't login.  Show them a message?
                log.info("账户被锁定，无法登录");
                return;
            }  catch ( AuthenticationException ae ) {
                //unexpected condition - error?
                log.info("未知错误...");
                return;
            }

        }
        Session session = currentUser.getSession();

        String id = (String) session.getId();
        System.out.println(id);
        System.out.println(session.getHost());

        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
