package control.db;

import models.dto.*;

import java.sql.SQLException;
import java.util.List;

public class DownloadService extends DbService {
    public static List<User> getAllUsers() throws SQLException {
        return userDao.getAll();
    }
    public static User getUserById(int usid) throws SQLException {

        return userDao.getEntityById(usid);
    }
    public static UserInfo getUserInfoById(int usid) throws SQLException {
        return userDao.getUserInfoById(usid);
    }

    public static List<RealEstate> getAllEstatesByUserId(int usid) throws SQLException {
        return estateDao.getAllFromUser(usid);
    }
    public static RealEstate getRealEstateById(int eid) throws SQLException{
        return estateDao.getEntityById(eid);
    }
    public static EstateStatus getEstateStatusById(int eid) throws SQLException {
        return estateDao.getEstateStatusById(eid);
    }

    public static List<Income> getAllIncomeByEstateId(int eid) throws SQLException {
        return incomeDao.getAllFromEstate(eid);
    }
    public static Income getIncomeById(int iid) throws SQLException {
        return incomeDao.getEntityById(iid);
    }

    public static List<Outcome> getAllOutcomeByEstateId(int eid) throws SQLException {
        return outcomeDao.getAllFromEstate(eid);
    }
    public static Outcome getOutcomeById(int oid) throws SQLException {
        return outcomeDao.getEntityById(oid);
    }
}
