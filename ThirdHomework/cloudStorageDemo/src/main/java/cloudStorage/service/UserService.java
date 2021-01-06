package cloudStorage.service;

import cloudStorage.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 用户管理的业务接口
 */
public interface UserService {

    /**
     * 登录方法
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 保存User
     * @param user
     */
    Boolean register(User user);

    Boolean findUserIsVip(String  username);

}
