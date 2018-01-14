package com.zhidi.dao;

import com.zhidi.entity.User;
import com.zhidi.util.DBUitl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/1/14/014.
 */
public class LoginDao {

    public User login(String username) throws SQLException {
        PreparedStatement ps = DBUitl.getConnection().prepareStatement("select * from tb_users where user_name = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        User user = null;
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String userName = rs.getString("user_name");
            String password = rs.getString("password");
            String phone = rs.getString("phone");
            user = new User(id, userName, password, phone);
        }
        return user;
    }
}
