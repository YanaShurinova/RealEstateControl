package control.db;

import control.db.dao.EstateDao;
import control.db.dao.IncomeDao;
import control.db.dao.OutcomeDao;
import control.db.dao.UserDao;

public abstract class DbService extends ConnectionMaster{
    protected static final UserDao userDao;
    protected static final EstateDao estateDao;
    protected static final IncomeDao incomeDao;
    protected static final OutcomeDao outcomeDao;

    static {
        userDao = new UserDao();
        estateDao = new EstateDao();
        incomeDao = new IncomeDao();
        outcomeDao = new OutcomeDao();
    }
}
