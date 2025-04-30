package myz.myDBLoading2;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import myz.myDBLoading.DBConnection;
import myz.myDBLoading.InsertMethods;
import myz.myDBLoading.MyUsedSQLs;
import myz.myDBLoading.StatementPreparer;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class GenCSVToLoad {
    public static final int BATCH_SIZE = 2000;
    public static int n = 0;

    public static void main(String[] args) {

        DBConnection dbCon = new DBConnection("src/resources/dbUser.properties");

        //initializing
        StatementPreparer NoConstraintTable = new StatementPreparer(dbCon, MyUsedSQLs.NO_CONSTRAINT_TABLE);
        NoConstraintTable.doCleanDataSentence();

        StatementPreparer stSupplyCenter1 = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_SUPPLY_CENTERS_RAW);
        StatementPreparer stProduct1 = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_PRODUCT_RAW);
        StatementPreparer stSalesman1 = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_SALESMAN_RAW);
        StatementPreparer stCompany1 = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_COMPANY_RAW);
        StatementPreparer stProductModel1 = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_PRODUCT_MODEL_RAW);
        StatementPreparer stContracts1 = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_CONTRACTS_RAW);
        StatementPreparer stOrders1 = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_ORDERS_RAW);



        //generate a middle table(csv)
        long start1 = System.currentTimeMillis();
        long end1;
        try ( CSVReader reader1 = new CSVReader(new FileReader("src/resources/output25S.csv"));
              CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter("src/resources/temp.csv"))))
            {
            String[] originHeader = reader1.readNext();
            String[] newHeader = new String[]{
                "contract number","client enterprise","supply center","country","city",                     //0-4
                "industry","product code","product name","product model","unit price",                      //5-9
                "quantity","contract date","estimated delivery date","lodgement date","director",           //10-14
                "salesman","salesman number","gender","age","mobile phone", "",                             //15-20
                "supplyCenterID", "salesmanID", "productID", "companyID", "productModelID", "contractID"    //21-26

            };
            writer.writeNext(newHeader);


            GenID tool1 = new GenID();

            String[] line;
            while ((line=reader1.readNext()) != null){
                tool1.genID(line);
                tool1.alterGender(line);
//                System.out.println(Arrays.toString(line));

                String[] ids = Arrays.stream(tool1.getID()).mapToObj(Integer::toString).toArray(String[]::new);
                String[] data = Stream.concat(Arrays.stream(line),Arrays.stream(ids)).toArray(String[]::new);

                writer.writeNext(data);

            }

            end1 = System.currentTimeMillis();
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Cost : " + (end1 - start1) / 1000L + " seconds");

        long start2 = System.currentTimeMillis();
        try (CSVReader reader2 = new CSVReader(new FileReader("src/resources/temp.csv"));){
            reader2.readNext();


            String[] values;
            while ((values=reader2.readNext())!= null){
                int contractId=Integer.parseInt(values[26]);
                int modelId=Integer.parseInt(values[25]);
                int salesmanId=Integer.parseInt(values[22]);

                insertSupplyCenter(stSupplyCenter1,values);
                insertProduct(stProduct1,values);
                insertSalesman(stSalesman1,values,salesmanId);
                insertCompany(stCompany1,values);
                insertProductModel(stProductModel1,values,modelId);
                insertContract(stContracts1,values,contractId);
                insertOrder(stOrders1,values,n,contractId,modelId,salesmanId);
                n++;


                if (n % BATCH_SIZE==0){
                    stSupplyCenter1.execute();
                    stProduct1.execute();
                    stSalesman1.execute();
                    stCompany1.execute();
                    stProductModel1.execute();
                    stContracts1.execute();
                    stOrders1.execute();
                }
            }

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("Cost : " + (end2 - start2) / 1000L + " seconds");

        StatementPreparer generateAlteredTable = new StatementPreparer(dbCon, MyUsedSQLs.GENERATE_ALTERED_TABLE);
        generateAlteredTable.doCleanDataSentence();

        long end3 = System.currentTimeMillis();

        System.out.println("Total Cost : " + (end2 - start1) / 1000L + " seconds");
        System.out.println("Loading speed : " + (500000 * 1000L) / (end2 - start1) + " records/s");


    }


//"contract number","client enterprise","supply center","country","city",                     //0-4
//"industry","product code","product name","product model","unit price",                      //5-9
//"quantity","contract date","estimated delivery date","lodgement date","director",           //10-14
//"salesman","salesman number","gender","age","mobile phone", "",                             //15-20
//"supplyCenterID", "salesmanID", "productID", "companyID", "productModelID", "contractID"    //21-26




    public static void insertSupplyCenter(StatementPreparer st,String[] values ){
        PreparedStatement stmt= st.getpStmt();
        try {



            stmt.setInt(1,Integer.parseInt(values[21]));
            stmt.setString(2,values[2]);
            stmt.setString(3,values[14]);

            stmt.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void insertProduct(StatementPreparer st,String[] values ){
        PreparedStatement stmt= st.getpStmt();

        try {
            stmt.setInt(1,Integer.parseInt(values[23]));
            stmt.setString(2,values[6]);
            stmt.setString(3,values[7]);

            stmt.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void insertSalesman(StatementPreparer st,String[] values,int salesmanID ){
        PreparedStatement stmt= st.getpStmt();
        try {
            stmt.setInt(1,salesmanID);
            stmt.setInt(2,Integer.parseInt(values[21]));
            stmt.setString(3,values[16]);
            stmt.setString(4,values[15]);
            stmt.setString(5,values[17]);
            stmt.setInt(6,Integer.parseInt(values[18]));
            stmt.setString(7,values[19]);

            stmt.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void insertCompany(StatementPreparer st,String[] values ){
        PreparedStatement stmt= st.getpStmt();

        try {
            stmt.setInt(1,Integer.parseInt(values[24]));
            stmt.setInt(2,Integer.parseInt(values[21]));
            stmt.setString(3,values[1]);
            stmt.setString(4,values[3]);
            stmt.setString(5,values[4]);
            stmt.setString(6,values[5]);

            stmt.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertProductModel(StatementPreparer st,String[] values, int modelId){
        PreparedStatement stmt= st.getpStmt();

        try {
            stmt.setInt(1,modelId);
            stmt.setInt(2,Integer.parseInt(values[23]));
            stmt.setString(3,values[8]);
            stmt.setInt(4,Integer.parseInt(values[9]));

            stmt.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertContract(StatementPreparer st,String[] values,int contractId){
        PreparedStatement stmt= st.getpStmt();

        try {
            stmt.setInt(1,contractId);
            stmt.setString(2,values[0]);
            stmt.setInt(3,Integer.parseInt(values[24]));
            if (values[11].isEmpty()) {
                stmt.setDate(4,null);
            }else {
                java.sql.Date date = java.sql.Date.valueOf(values[11]);
                stmt.setDate(4,date);
            };

            stmt.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void insertOrder(StatementPreparer st,String[] values, int n,int contractId, int modelId, int salesmanID){
        PreparedStatement stmt= st.getpStmt();

        try {
            stmt.setInt(1,n);
            stmt.setInt(2,contractId);
            stmt.setInt(3,modelId);
            stmt.setInt(4,salesmanID);
            stmt.setInt(5,Integer.parseInt(values[10]));
            if (values[12].isEmpty()) {
                stmt.setDate(6,null);
            }else {
                java.sql.Date date = java.sql.Date.valueOf(values[12]);
                stmt.setDate(6,date);
            };
            if (values[13].isEmpty()) {
                stmt.setDate(7,null);
            }else {
                java.sql.Date date = java.sql.Date.valueOf(values[13]);
                stmt.setDate(7,date);
            };

            stmt.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
