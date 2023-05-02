package models.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class UserInfo implements Serializable {
    private Integer uid;
    private String name;
    private LocalDate regd;
    private String desc;

    public UserInfo(Integer uid, String name, LocalDate regd, String desc) {
        this.uid = uid;
        this.name = name;
        this.regd = regd;
        this.desc = desc;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getRegd() {
        return regd;
    }

    public void setRegd(LocalDate regd) {
        this.regd = regd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo user_info = (UserInfo) o;
        return uid == user_info.uid && name.equals(user_info.name) && regd.equals(user_info.regd) && desc.equals(user_info.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, regd, desc);
    }

    @Override
    public String toString() {
        return "User_info{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", regd=" + regd +
                ", desc='" + desc + '\'' +
                '}';
    }

    public UserInfo clone(){
        return new UserInfo(uid, name, regd, desc);
    }
}
