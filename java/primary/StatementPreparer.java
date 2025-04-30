package myz.myDBLoading;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementPreparer {
    private final DBConnection dbCon;
    private final Connection con;
    private PreparedStatement pStmt;
    private MyUsedSQLs sqlSentence;

    public StatementPreparer(DBConnection dbC,MyUsedSQLs sql) {
        this.dbCon = dbC;
        this.con = dbC.getConnection();
        this.sqlSentence= sql;
        prepareStatement();
    }

    private void prepareStatement(){
        try {
            pStmt=con.prepareStatement(sqlSentence.getSql());
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            dbCon.closeDB();
            System.exit(1);
        }
    }

    public void fillOneData(String data, UsedDataType type, int placeInSQL){
        if (con != null){

            try {
                switch (type){
                    case INT:
                        pStmt.setInt(placeInSQL,Integer.parseInt(data));
                        break;
                    case STRING:
                        pStmt.setString(placeInSQL,data);
                        break;
                    case DOUBLE:
                        pStmt.setDouble(placeInSQL,Double.parseDouble(data));
                        break;
                    case DATE:
                        if (data.isEmpty()) {
                            pStmt.setDate(placeInSQL,null);
                        }else {
                            java.sql.Date date = java.sql.Date.valueOf(data);
                            pStmt.setDate(placeInSQL,date);
                        }
                        break;
                    default:
                        System.err.println("load data type issue");;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }else {
            System.err.println("No connection");
            System.exit(1);
        }

    }

    public void update(){
        try {
            pStmt.executeUpdate();
//            System.out.println(sqlSentence+" Updated");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(){
        try {
            pStmt.executeBatch();
            System.out.println(sqlSentence+" Successfully executed");
            con.commit();
            pStmt.clearBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void doCleanDataSentence(){
        try {
            pStmt.executeUpdate();
            con.commit();
            System.out.println(sqlSentence+" Updated");
            pStmt.close();
            System.out.println(sqlSentence+" closed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MyUsedSQLs getSqlSentence() {
        return sqlSentence;
    }

    public void setSqlSentence(MyUsedSQLs sqlSentence) {
        this.sqlSentence = sqlSentence;
    }
}
