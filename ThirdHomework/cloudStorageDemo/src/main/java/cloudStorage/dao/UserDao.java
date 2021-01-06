package cloudStorage.dao;

import cloudStorage.domain.User;
import cloudStorage.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 操作数据库中User表的类
 */

/**
 * 用户操作的DAO
 */
public interface UserDao {


    User findUserByUsernameAndPassword(String username, String password);

    void add(User user);

    Boolean findUserByUsername(String username);

    Boolean findIsVipByUsername(String username);
}

