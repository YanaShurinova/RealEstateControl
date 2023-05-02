package control.db.dao;

import control.db.ConnectionMaster;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao<E, K> extends ConnectionMaster {


    public abstract List<E> getAll() throws SQLException;

    public abstract PreparedStatement update(E entity) throws SQLException;

    public abstract E getEntityById(K id) throws SQLException;

    public abstract PreparedStatement delete(K id) throws SQLException;

    public abstract PreparedStatement create(E entity) throws SQLException;

    public abstract Integer getNewId() throws SQLException;
}
