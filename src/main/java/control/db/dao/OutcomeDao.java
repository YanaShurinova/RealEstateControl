package control.db.dao;

import models.dto.Outcome;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OutcomeDao extends AbstractDao<Outcome, Integer>{
    private static final String OID_SEQ_NEXTVAL = "select oid_seq.nextval from DUAL";

    private static final String SELECT_ALL_OUTCOME = "select * from outcome order by odate desc";
    private static final String SELECT_OUTCOME_BY_ID = "select * from outcome where oid = ?";
    private static final String SELECT_OUTCOME_BY_ESTATE_ID = "select * from outcome where eid = ? order by odate desc";
    private static final String INSERT_OUTCOME = "insert into outcome values (?,?,?,?,?,?)";
    private static final String UPDATE_OUTCOME_BY_ID = "update outcome set odate = ?, name = ?, value = ?, ocomment = ? where oid = ?";
    private static final String DELETE_OUTCOME_BY_ID = "delete from outcome where oid = ?";
    private static final String DELETE_ALL_OUTCOME_BY_ESTATE_ID = "delete from outcome where eid = ? ";
    private static final String DELETE_ALL_OUTCOME_BY_USER_ID = "delete from outcome where eid in " +
            "(select eid from real_estate where usid = ?)";

    @Override
    public List<Outcome> getAll() throws SQLException {
        Statement st = connection.createStatement();
        ResultSet resultSet = st.executeQuery(SELECT_ALL_OUTCOME);

        List<Outcome> outcomes = new ArrayList<Outcome>();
        while(resultSet.next()){
            outcomes.add(new Outcome(
                    resultSet.getInt("OID"),
                    resultSet.getInt("EID"),
                    resultSet.getDate("ODATE").toLocalDate(),
                    resultSet.getString("NAME"),
                    resultSet.getDouble("VALUE"),
                    resultSet.getString("OCOMMENT")
            ));
        }
        st.close();
        return outcomes;
    }

    public List<Outcome> getAllFromEstate(Integer eid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(SELECT_OUTCOME_BY_ESTATE_ID);
        pr.setInt(1, eid);
        ResultSet resultSet = pr.executeQuery();

        List<Outcome> outcomes = new ArrayList<Outcome>();
        while(resultSet.next()){
            outcomes.add(new Outcome(
                    resultSet.getInt("OID"),
                    resultSet.getInt("EID"),
                    resultSet.getDate("ODATE").toLocalDate(),
                    resultSet.getString("NAME"),
                    resultSet.getDouble("VALUE"),
                    resultSet.getString("OCOMMENT")
            ));
        }
        pr.close();
        return outcomes;
    }

    @Override
    public PreparedStatement update(Outcome outcome) throws SQLException {
        Outcome oldOutcome = getEntityById(outcome.getOid());
        PreparedStatement pr = connection.prepareStatement(UPDATE_OUTCOME_BY_ID);

        if (outcome.getOdate() != null){
            pr.setDate(1, Date.valueOf(outcome.getOdate()));
        } else {
            pr.setDate(1, Date.valueOf(oldOutcome.getOdate()));
        }

        if (outcome.getName() != null){
            pr.setString(2, outcome.getName());
        } else {
            pr.setString(2, oldOutcome.getName());
        }

        if (outcome.getValue() != null){
            pr.setDouble(3, outcome.getValue());
        } else {
            pr.setDouble(3, oldOutcome.getValue());
        }

        if (outcome.getOcomment() != null){
            pr.setString(4, outcome.getOcomment());
        } else {
            pr.setString(4, oldOutcome.getOcomment());
        }

        pr.setInt(5, outcome.getOid());

        return pr;
    }

    @Override
    public Outcome getEntityById(Integer oid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(SELECT_OUTCOME_BY_ID);
        pr.setInt(1, oid);
        ResultSet resultSet = pr.executeQuery();

        Outcome outcome = null;
        if(resultSet.next()){
            outcome = new Outcome(
                    resultSet.getInt("OID"),
                    resultSet.getInt("EID"),
                    resultSet.getDate("ODATE").toLocalDate(),
                    resultSet.getString("NAME"),
                    resultSet.getDouble("VALUE"),
                    resultSet.getString("OCOMMENT")
            );
        }

        pr.close();
        return outcome;
    }

    @Override
    public PreparedStatement delete(Integer oid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_OUTCOME_BY_ID);
        pr.setInt(1, oid);

        return pr;
    }

    public PreparedStatement deleteAllOutcomeByEstateId(Integer eid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_ALL_OUTCOME_BY_ESTATE_ID);
        pr.setInt(1, eid);

        return pr;
    }

    public PreparedStatement deleteAllOutcomeByUserId(Integer usid) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(DELETE_ALL_OUTCOME_BY_USER_ID);
        pr.setInt(1, usid);

        return pr;
    }

    @Override
    public PreparedStatement create(Outcome outcome) throws SQLException {
        PreparedStatement pr = connection.prepareStatement(INSERT_OUTCOME);
        pr.setInt(1, outcome.getOid());
        pr.setInt(2, outcome.getEid());
        pr.setDate(3, Date.valueOf(outcome.getOdate()));
        pr.setString(4, outcome.getName());
        pr.setDouble(5, outcome.getValue());
        pr.setString(6, outcome.getOcomment());

        return pr;
    }

    @Override
    public Integer getNewId() throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(OID_SEQ_NEXTVAL);
        Integer oid = null;
        if(rs.next()) {
            oid = rs.getInt(1);
        }
        st.close();
        return oid;
    }
}
