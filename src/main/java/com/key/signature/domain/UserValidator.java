package com.key.signature.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by pyshankov on 22.05.2016.
 */
public class UserValidator  implements Validator{

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        //handle trouble with
//        final User u = (User) o;
//        if (userRepository.findByUserName(u.getUserName())!=null){
//            errors.rejectValue("exist", "User.unique", "User already exist");
//        }
    }
}
