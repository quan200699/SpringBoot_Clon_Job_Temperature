package com.codegym.demo.service.user;

import com.codegym.demo.model.User;
import com.codegym.demo.service.IGeneralService;

public interface IUserService extends IGeneralService<User> {
    Iterable<User> findAllByEnableIsTrue();

    Iterable<User> findAllByEnableIsFalse();
}
