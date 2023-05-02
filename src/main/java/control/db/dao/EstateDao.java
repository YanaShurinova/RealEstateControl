package control.db.dao;

import models.dto.EstateStatus;
import models.dto.RealEstate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstateDao extends AbstractDao<RealEstate, Integer>{

    private static final String EID_SEQ_NEXTVAL = "select eid_seq.nextval from DUAL";

    private static final String SELECT_ALL_FROM_REAL_ESTATE = "select * from real_estate";
    private static final String SELECT_REAL_ESTATE_BY_ID = "select * from real_estate where eid = ?";
    private static final String SELECT_REAL_ESTATE_BY_USER_ID = "select * from real_estate where usid = ?";
    private static final String INSERT_INTO_REAL_ESTATE = "insert into real_estate values (?,?,?,?)";
    private static final String UPDATE_REAL_ESTATE_BY_ID = "update real_estate set type = ?, address = ? where eid = ?";
    private static final String DELETE_FROM_REAL_ESTATE_BY_ID = "delete from real_estate where eid = ?";
    private static final String DELETE_FROM_REAL_ESTATE_BY_USER_ID = "delete from real_estate where usid = ?";

    private static final String SELECT_ESTATE_STATUS_BY_ID = "select * from estate_status where eid = ?";
    private static final String INSERT_INTO_ESTATE_STATUS = "insert into estate_status values(?,?,?,?,?,?)";
    private static final String UPDATE_ESTATE_STATUS_BY_ID = "update estate_status set purchase_p = ?, purchase_d = ?," +
            " sold = ?, sold_p = ?, sold_d = ? where eid = ?";
    private static final String DELETE_FROM_ESTATE_STATUS_BY_ID = "delete from estate_status where eid = ?";
    private static final String DELETE_FROM_ESTATE_STATUS_BY_USER_ID = "delete from estate_status where eid in" +
            " (select eid from real_estate where usid = ?)";

    @Override
    public List<RealEstate> getAll() throws SQLException {
        Statement st =connection.createStatement();
        ResultSet resultSet = st.executeQuery(SELECT_ALL_FROM_REAL_ESTATE);

        List<RealEstate> realEstates = new ArrayList<RealEstate>();
        while (resultSet.next()){
            realEstates.add(new RealEstate(
                    resultSet.getInt("EID"),
                    resultSet.getInt("USID"),
                    resultSet.getString("TYPE"),
                    resultSet.getString("ADDRESS")
            ));
        }

        st.close();
        return realEstates;
    }

    public List<RealEstate> getAllFromUser(Integer usid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(SELECT_REAL_ESTATE_BY_USER_ID);
        pr.setInt(1, usid);
        ResultSet resultSet = pr.executeQuery();

        List<RealEstate> realEstates = new ArrayList<RealEstate>();
        while (resultSet.next()){
            realEstates.add(new RealEstate(
                    resultSet.getInt("EID"),
                    resultSet.getInt("USID"),
                    resultSet.getString("TYPE"),
                    resultSet.getString("ADDRESS")
            ));
        }

        pr.close();
        return realEstates;
    }

    @Override
    public PreparedStatement update(RealEstate estate) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(UPDATE_REAL_ESTATE_BY_ID);
        RealEstate oldEstate = getEntityById(estate.getEid());

        if (estate.getType() != null){
            pr.setString(1, estate.getType());
        } else {
            pr.setString(1, oldEstate.getType());
        }

        if (estate.getAddress() != null){
            pr.setString(2, estate.getAddress());
        } else {
            pr.setString(2, oldEstate.getAddress());
        }
        pr.setInt(3, estate.getEid());

        return pr;
    }

    public PreparedStatement updateEstateStatus(EstateStatus status) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(UPDATE_ESTATE_STATUS_BY_ID);
        EstateStatus oldStatus = getEstateStatusById(status.getEid());
        if (status.getPurchasePrice() != null){
            pr.setInt(1, status.getPurchasePrice());
        } else {
            pr.setInt(1, oldStatus.getPurchasePrice());
        }

        if (status.getPurchaseDate() != null){
            pr.setDate(2, Date.valueOf(status.getPurchaseDate()));
        } else {
            pr.setDate(2, Date.valueOf(oldStatus.getPurchaseDate()));
        }

        if (status.isSold() != null){
            pr.setString(3, status.isSold()?"1":"0");
        } else {
            pr.setString(3, oldStatus.isSold()?"1":"0");
        }

        if (status.isSold()) {
            if (status.getSoldPrice() != null) {
                pr.setInt(4, status.getSoldPrice());
            } else {
                if (oldStatus.getSoldPrice() == null) {
                    pr.setNull(4, Types.NUMERIC);
                } else {
                    pr.setInt(4, oldStatus.getSoldPrice());
                }
            }
            if (status.getSoldDate() != null) {
                pr.setDate(5, Date.valueOf(status.getSoldDate()));
            } else {
                pr.setDate(5, Date.valueOf(oldStatus.getSoldDate()));
            }
        } else {
            pr.setNull(4, Types.NUMERIC);
            pr.setNull(5, Types.DATE);
        }
        pr.setInt(6, status.getEid());

        return pr;
    }

    @Override
    public RealEstate getEntityById(Integer eid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(SELECT_REAL_ESTATE_BY_ID);
        pr.setInt(1, eid);
        ResultSet resultSet = pr.executeQuery();

        RealEstate realEstate = null;
        if (resultSet.next()){
            realEstate = new RealEstate(
                    resultSet.getInt("EID"),
                    resultSet.getInt("USID"),
                    resultSet.getString("TYPE"),
                    resultSet.getString("ADDRESS")
            );
        }

        pr.close();
        return realEstate;
    }

    public EstateStatus getEstateStatusById(Integer eid) throws SQLException{
        PreparedStatement pr = connection.prepareStatement(SELECT_ESTATE_STATUS_BY_ID);
        pr.setInt(1, eid);
        ResultSet resultSet = pr.executeQuery();

        EstateStatus estateStatus = null;
        if (resultSet.next()){
            estateStatus = new EstateStatus(
                    resultSet.getInt("EID"),
                    resultSet.getInt("PURCHASE_P"),
                    resultSet.getDate("PURCHASE_D").toLocalDate(),
                    resultSet.getString("SOLD")=="1",
                    resultSet.getInt("SOLD_P"),
                    resultSet.getDate("SOLD_D")==null?null:resultSet.getDate("SOLD_D").toLocalDate()
            );
        }
        pr.close();
        return estateStatus;
    }

    @Override
    public PreparedStatement delete(Integer eid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_FROM_REAL_ESTATE_BY_ID);
        pr.setInt(1, eid);

        return pr;
    }

    public PreparedStatement deleteEstateStatusById(Integer eid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_FROM_ESTATE_STATUS_BY_ID);
        pr.setInt(1, eid);

        return pr;
    }

    public PreparedStatement deleteAllRealEstateByUserID(Integer usid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_FROM_REAL_ESTATE_BY_USER_ID);
        pr.setInt(1, usid);

        return pr;
    }

    public PreparedStatement deleteAllEstateStatusByUserId(Integer usid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_FROM_ESTATE_STATUS_BY_USER_ID);
        pr.setInt(1, usid);

        return pr;
    }
    @Override
    public PreparedStatement create(RealEstate estate) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(INSERT_INTO_REAL_ESTATE);
        pr.setInt(1, estate.getEid());
        pr.setInt(2, estate.getUsid());
        pr.setString(3, estate.getType());
        pr.setString(4, estate.getAddress());

        return pr;
    }

    public PreparedStatement createEstateStatus(EstateStatus status) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(INSERT_INTO_ESTATE_STATUS);
        pr.setInt(1, status.getEid());
        pr.setInt(2, status.getPurchasePrice());
        pr.setDate(3, Date.valueOf(status.getPurchaseDate()));
        pr.setString(4, (status.isSold())?"1":"0");
        if (status.isSold()){
            pr.setInt(5, status.getSoldPrice());
            pr.setDate(6, Date.valueOf(status.getSoldDate()));
        } else {
            pr.setNull(5, Types.NUMERIC);
            pr.setNull(6, Types.DATE);
        }

        return pr;
    }

    @Override
    public Integer getNewId() throws SQLException {
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery(EID_SEQ_NEXTVAL);

        Integer eid = null;
        if (resultSet.next()){
            eid = resultSet.getInt(1);
        }
        st.close();
        return eid;
    }
}
