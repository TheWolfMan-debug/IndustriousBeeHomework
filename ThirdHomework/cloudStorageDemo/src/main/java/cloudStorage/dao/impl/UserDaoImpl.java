package cloudStorage.dao.impl;

import cloudStorage.dao.UserDao;
import cloudStorage.domain.User;
import cloudStorage.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


public class UserDaoImpl implements UserDao {

    //声明JDBCTemplate对象共用
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {

        try {
            String sql = "select * from user where username = ? and password = ?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username, password);
            return user;

        } catch (Exception e) {
            return null;
        }

    }


    @Override
    public void add(User user) {
        //1.定义sql
        String sql = "INSERT INTO USER(USERNAME,PASSWORD,VIP) VALUES(?,?,?);";
        //2.执行sql
        template.update(sql, user.getUsername(), user.getPassword(),user.getVip());
    }

    @Override
    public Boolean findUserByUsername(String username) {
        try {
            String sql = "select * from user where username = ?";
            template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean findIsVipByUsername(String username) {
        try {
            String sql = "select * from user where username = ? && VIP='1' ";
            template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


