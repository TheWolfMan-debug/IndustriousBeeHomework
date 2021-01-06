package cloudStorage.service.impl;

import cloudStorage.dao.UserDao;
import cloudStorage.dao.impl.UserDaoImpl;
import cloudStorage.domain.User;
import cloudStorage.service.UserService;
import cloudStorage.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;


public class UserServiceImpl implements UserService {
    private UserDaoImpl dao = new UserDaoImpl();

    @Override
    public User login(User user) {
        return dao.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @Override
    public Boolean register(User user) {
        if("".equals(user.getPassword())||"".equals(user.getUsername()))
        {
            return false;
        }
        if ((!dao.findUserByUsername(user.getUsername()))) {
            dao.add(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean findUserIsVip(String username) {
        return dao.findIsVipByUsername(username);
    }
}
