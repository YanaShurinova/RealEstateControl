package control.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction {
    private PreparedStatement[] statements;
    private boolean isClosed = false;

    public Transaction(){}
    public Transaction(PreparedStatement ... args) throws SQLException {
        boolean isClosed = false;
        for (PreparedStatement st: args){
            isClosed= isClosed||st.isClosed();
        }
        if (isClosed){
            throw new IllegalArgumentException("Closed statement");
        }
        statements = args;
    }

    public boolean execute() throws SQLException {
        if (statements == null){
            throw new NullPointerException("Null transaction execution");
        }
        boolean result = true;
        boolean temp;
        for(PreparedStatement st: statements){
            temp = st.executeUpdate() > 0;
            result = result && temp;
        }
        return result;
    }

    public void close() throws SQLException {
        if (statements == null){
            throw new NullPointerException("Null transaction was tried to be closed");
        }
        if (!isClosed){
            for(PreparedStatement st: statements){
                st.close();
            }
            isClosed = true;
        }
    }

}
