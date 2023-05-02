package control.db.dao;

import models.dto.User;
import models.dto.UserInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao extends AbstractDao<User, Integer>{

    private static final String UID_SEQ_NEXTVAL = "select uid_seq.nextval from DUAL";

    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String SELECT_USER_BY_ID = "select * from users where usid = ?";
    private static final String DELETE_USER_BY_ID = "delete from users where usid = ?";
    private static final String INSERT_USER = "insert into users values(?, ?, ?)";
    private static final String UPDATE_USER_BY_ID = "update users set login = ?, password = ? where usid = ?";

    private static final String SELECT_USER_INFO_BY_ID = "select * from user_info where usid = ?";
    private static final String DELETE_USER_INFO_BY_ID = "delete from user_info where usid = ?";
    private static final String INSERT_USER_INFO = "insert into user_info values(?, ?, ?, ?)";
    private static final String UPDATE_USER_INFO_BY_ID = "update user_info set name = ?, regd = ?, dscr = ? where usid = ?";

    private static final String SELECT_ALL_LOGINS = "select login from users";

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery(SELECT_ALL_USERS);
        while(resultSet.next()){
            users.add(new User(
                    resultSet.getInt("USID"),
                    resultSet.getString("LOGIN"),
                    Integer.parseInt(resultSet.getString("PASSWORD"))
            ));
        }
        st.close();
        return users;
    }

    //user
    @Override
    public PreparedStatement update(User user) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(UPDATE_USER_BY_ID);
        User oldUser = getEntityById(user.getUid());

        if (user.getLogin() == null){
            pr.setString(1, oldUser.getLogin());
        } else {
            pr.setString(1, user.getLogin());
        }

        if (user.getPassword() == null){
            pr.setString(2, oldUser.getPassword().toString());
        } else {
            pr.setString(2, user.getPassword().toString());
        }

        pr.setInt(3, user.getUid());

        return pr;
    }

    //userInfo
    public PreparedStatement updateUserInfo(UserInfo info) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(UPDATE_USER_INFO_BY_ID);
        UserInfo oldInfo = getUserInfoById(info.getUid());

        if (info.getName() == null){
            pr.setString(1, oldInfo.getName());
        } else {
            pr.setString(1, info.getName());
        }

        if (info.getRegd() == null){
            pr.setDate(2, Date.valueOf(oldInfo.getRegd()));
        } else {
            pr.setDate(2, Date.valueOf(info.getRegd()));
        }

        if (info.getDesc() == null){
            pr.setString(3, oldInfo.getDesc());
        } else {
            pr.setString(3, info.getDesc());
        }

        pr.setInt(4, info.getUid());

        return pr;
    }

    // user
    @Override
    public User getEntityById(Integer id) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(SELECT_USER_BY_ID);
        pr.setInt(1, id);
        ResultSet resultSet = pr.executeQuery();

        User user = null;
        if (resultSet.next()){
            user = new User(
                    resultSet.getInt("USID"),
                    resultSet.getString("LOGIN"),
                    Integer.parseInt(resultSet.getString("PASSWORD"))
            );
        }
        pr.close();
        return user;
    }

    // userInfo
    public UserInfo getUserInfoById(Integer id) throws SQLException{
        PreparedStatement pr = connection.prepareStatement(SELECT_USER_INFO_BY_ID);
        pr.setInt(1, id);
        ResultSet resultSet = pr.executeQuery();

        UserInfo info = null;
        if (resultSet.next()){
            info = new UserInfo(
                    resultSet.getInt("USID"),
                    resultSet.getString("NAME"),
                    resultSet.getDate("REGD").toLocalDate(),
                    resultSet.getString("DSCR")
            );
        }
        pr.close();
        return info;
    }

    //user + userInfo
    @Override
    public PreparedStatement delete(Integer id) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_USER_BY_ID);
        pr.setInt(1, id);

        return pr;
    }

    public PreparedStatement deleteUserInfoById(Integer id) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_USER_INFO_BY_ID);
        pr.setInt(1, id);

        return pr;
    }

    @Override
    public Integer getNewId() throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(UID_SEQ_NEXTVAL);
        Integer uid = null;
        if(rs.next()) {
            uid = rs.getInt(1);
        }
        st.close();
        return uid;
    }

    //user
    @Override
    public PreparedStatement create(User user) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(INSERT_USER);
        pr.setInt(1, user.getUid());
        pr.setString(2, user.getLogin());
        pr.setString(3, user.getPassword().toString());

        return pr;
    }

    //userInfo
    public PreparedStatement createUserInfo(UserInfo info) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(INSERT_USER_INFO);
        pr.setInt(1, info.getUid());
        pr.setString(2, info.getName());
        pr.setDate(3, Date.valueOf(info.getRegd()));
        pr.setString(4, info.getDesc());

        return pr;
    }

    public List<String> getAllLogins() throws SQLException {
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery(SELECT_ALL_LOGINS);
        List<String> logins = new ArrayList<String>();

        while(resultSet.next()){
            logins.add(resultSet.getString(1));
        }

        st.close();
        return logins;
    }
}
