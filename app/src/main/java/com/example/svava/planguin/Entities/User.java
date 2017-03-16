package com.example.svava.planguin.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Svava on 14.03.17.
 */

public class User {

    private List<User> friends = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private String username;
    private int userId;
    private String photo;  // Not used in this version
    private String school;
    private String password;
    private String passwordConfirm;  // Only used in signup  - má taka upp

    public List<User> getFriends(){return friends;}
    public void addFriend(User user){friends.add(user);}
    public void removeFriend(User user){friends.remove(user);}

    public List<Group> getGroups() {return groups;}
    public void addGroup(Group group) {groups.add(group);}
    public void removeGroup(Group group) {groups.remove(group);}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public int getUserId(){return userId;}
    public void setUserId(int userId){this.userId = userId;}

    public String getPhoto(){return photo;}
    public void setPhoto(String photo){this.photo = photo;}

    public String getSchool(){return school;}
    public void setSchool(String school){this.school = school;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public String getPasswordConfirm(){return passwordConfirm;}
    public void setPasswordConfirm(String passwordConfirm){this.passwordConfirm = passwordConfirm;}
}
