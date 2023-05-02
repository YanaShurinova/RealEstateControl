package models.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class User implements Serializable {
    private Integer uid;
    private String login;
    private Integer password;

    private UserInfo info;
    private HashMap<Integer, RealEstate> estates = new HashMap<>();

    public User(Integer id, String login, Integer password) {
        this.uid = id;
        this.login = login;
        this.password = password;
    }

    public List<RealEstate> getAllEstates(){
        return new ArrayList<RealEstate>(estates.values());
    }

    public RealEstate getEstate(int eid){
        return estates.get(eid);
    }

    public void addEstate(RealEstate estate, EstateStatus status){
        if (estate.getEid()!=status.getEid() || estate.getUsid()!=uid){
            throw new IllegalArgumentException("УИД не совпадают");
        }
        estate.setStatus(status);
        estates.put(estate.getEid(), estate);
    }

    public void deleteEstate(int eid){
        estates.remove(eid);
    }

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }


    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uid == user.uid && password == user.password  && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, login, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + uid +
                ", name='" + login + '\'' +
                ", password=" + password +
                '}';
    }

    public User clone(){
        return new User(uid, login, password);
    }
}
