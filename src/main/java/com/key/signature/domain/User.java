package com.key.signature.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by pyshankov on 17.05.2016.
 */
@Entity
public class User implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "isActivated",nullable = false)
    private boolean isActivated;

    @Column(name = "keyHandWriting")
    private String keyHandWriting;

    public User(String userName,String password){
        this.userName=userName;
        this.password=password;
        id=-1;
        role=Role.USER;
        isActivated=false;
        keyHandWriting="";
    }

    public User(){};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public String getKeyHandWriting() {
        return keyHandWriting;
    }

    public void setKeyHandWriting(String keyHandWriting) {
        this.keyHandWriting = keyHandWriting;
    }

    public enum Role{
        ADMIN,
        USER
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", isActivated=" + isActivated +
                ", keyHandWriting='" + keyHandWriting + '\'' +
                '}';
    }
}