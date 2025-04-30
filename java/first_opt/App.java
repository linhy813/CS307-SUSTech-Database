package myz.myDBLoading;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;



public class App 
{
    public static int n = 0;

    public static void main( String[] args ){
        int BATCH_SIZE = 5000;
        //Connect to DB
        DBConnection dbCon = new DBConnection("src/resources/dbUser.properties");
        long start = System.currentTimeMillis();

        //start to read file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/resources/output25S.csv") );
            System.out.println(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            //clean the data
            StatementPreparer cleanAll = new StatementPreparer(dbCon, MyUsedSQLs.CLEAN_ALL_DATA);
            cleanAll.doCleanDataSentence();
            cleanAll = null;

            //initialize sentences
            StatementPreparer stSupplyCenter = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_SUPPLY_CENTERS);
            StatementPreparer stSalesman = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_SALESMAN);
            StatementPreparer stProduct = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_PRODUCT);
            StatementPreparer stCompany = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_COMPANY);
            StatementPreparer stProductModel = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_PRODUCT_MODEL);
            StatementPreparer stContracts = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_CONTRACTS);
            StatementPreparer stOrders = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_ORDERS);


            //do the loop
            try {
                String line;


                //read lines and loop
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

                    //fill in the statements
                    InsertMethods.insertSupplyCenter(values, stSupplyCenter);
                    InsertMethods.insertSalesman(values, stSalesman);
                    InsertMethods.insertProduct(values, stProduct);
                    InsertMethods.insertCompany(values, stCompany);
                    InsertMethods.insertProductModel(values, stProductModel);
                    InsertMethods.insertContract(values,stContracts);
                    InsertMethods.insertOrders(values,stOrders);


                    //改成每个分别判断

                    //do the statements if reach batch size
                    if (n % BATCH_SIZE == 0) {

                        stSupplyCenter.execute();
                        stSalesman.execute();
                        stProduct.execute();
                        stCompany.execute();
                        stProductModel.execute();
                        stContracts.execute();
                        stOrders.execute();

                        System.out.println("insert " + BATCH_SIZE + " data successfully!");
                    }

                    n++;
                }

                //execute left sentences

                stSupplyCenter.execute();
                stSalesman.execute();
                stProduct.execute();
                stCompany.execute();
                stProductModel.execute();
                stContracts.execute();
                stOrders.execute();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } catch (Exception e) {
            System.out.println(n);
            throw new RuntimeException(e);
        }

        //closing
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dbCon.closeDB();

        long end = System.currentTimeMillis();
        System.out.println("Cost : "+(end - start)/1000L+" seconds");
        System.out.println("Data count : "+ n);
        System.out.println("Loading speed : " + (n * 1000L) / (end - start) + " records/s");

    }


}
