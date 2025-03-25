package com.vweinert.fedditbackend.service.inter;

import com.vweinert.fedditbackend.models.User;

public interface UserService {
    boolean isUserDeleted(User user) throws Exception;
    User registerUser(User user) throws Exception;
    User signInUser(User user)throws Exception;

    User updateUser(long id, User user) throws Exception;

    User whoami(long id) throws Exception;
}
