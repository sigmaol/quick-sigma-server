package org.ko.sigma.rest.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.ko.sigma.core.exception.BusinessException;
import org.ko.sigma.data.entity.User;
import org.ko.sigma.rest.user.condition.QueryUserCondition;
import org.ko.sigma.rest.user.dto.UserDTO;
import org.ko.sigma.rest.user.repository.UserRepository;
import org.ko.sigma.rest.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.ko.sigma.core.constant.SystemCode.*;

@Service
@Transactional(rollbackFor = Throwable.class)
public class UserServiceImpl extends ServiceImpl<UserRepository, User> implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public IPage<UserDTO> queryUserList(QueryUserCondition<User> condition) {
        return userRepository.queryUserList(condition);
    }

    @Override
    public User queryUserInfo(Long id) {
        return userRepository.selectById(id);
    }

    @Override
    public Long createUser(User user) {
        //加密password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.insert(user) == 0) {
            throw new BusinessException(UNKNOWN_ERROR);
        }
        return user.getId();
    }

    @Override
    public User updateUser(Long id, User user) {
        user.setId(id);
        if (userRepository.updateById(user) == 0) {
            throw new BusinessException(UNKNOWN_ERROR);
        }
        return user;
    }

    @Override
    public Long removeUser(Long id) {
        if (userRepository.deleteById(id) == 0) {
            throw new BusinessException(UNKNOWN_ERROR);
        }
        return id;
    }

}
