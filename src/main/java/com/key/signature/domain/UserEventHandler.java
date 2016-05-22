package com.key.signature.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
/**
 * Created by pyshankov on 22.05.2016.
 */
@Component
@RepositoryEventHandler
public class UserEventHandler {

    @Autowired
    private UserRepository userRepository;

    @HandleBeforeCreate
    public void setDefaultParameters(User u){
        if(u.getUserName()==null)   throw new UserExeption("name couldn't be null");
        if(userRepository.findByUserName(u.getUserName())!=null ) throw new UserExeption("User already exist");

        u.setPassword("");
        u.setActivated(true);
        u.setRole(User.Role.USER);
    }


}
