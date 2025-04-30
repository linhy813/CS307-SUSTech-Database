package myz.myDBLoading;

import java.io.*;




public class App {
    public static final int BATCH_SIZE = 1000;

    public static void main(String[] args) {

        //Connect to DB
        DBConnection dbCon = new DBConnection("src/resources/dbUser.properties");
        long start = System.currentTimeMillis();

        //start to read file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("src/resources/output25S.csv"));
            System.out.println(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            //clean the data
            StatementPreparer cleanAll = new StatementPreparer(dbCon, MyUsedSQLs.CLEAN_ALL_DATA);
            cleanAll.doCleanDataSentence();

            //initialize sentences
            StatementPreparer stSupplyCenter = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_SUPPLY_CENTERS);
            StatementPreparer stProduct = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_PRODUCT);
            StatementPreparer stSalesman = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_SALESMAN);
            StatementPreparer stCompany = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_COMPANY);
            StatementPreparer stProductModel = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_PRODUCT_MODEL);
            StatementPreparer stContracts = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_CONTRACTS);
            StatementPreparer stOrders = new StatementPreparer(dbCon, MyUsedSQLs.INSERT_ORDERS);

            StatementPreparer[] statements = new StatementPreparer[]{stSupplyCenter, stProduct, stSalesman
                    , stCompany, stProductModel, stContracts, stOrders};


            //do the loop
            try {
                String line;


                //read lines and loop
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

                    InsertMethods.getPK(values);
                    //fill in the statements
                    InsertMethods.insertSupplyCenter(values, stSupplyCenter);
                    InsertMethods.insertSalesman(values, stSalesman);
                    InsertMethods.insertProduct(values, stProduct);
                    InsertMethods.insertCompany(values, stCompany);
                    InsertMethods.insertProductModel(values, stProductModel);
                    InsertMethods.insertContract(values, stContracts);
                    InsertMethods.insertOrders(values, stOrders);


                    //改成每个分别判断

                    //do the statements if reach batch size
                    if (stSupplyCenter.getDataCount() % BATCH_SIZE == 0) {
                        executingSupplyCenter(statements);
                    }
                    if (stProduct.getDataCount() % BATCH_SIZE == 0) {
                        executingProduct(statements);
                    }

                    if (stSalesman.getDataCount() % BATCH_SIZE == 0) {
                        executingSalesman(statements);
                    }
                    if (stCompany.getDataCount() % BATCH_SIZE == 0) {
                        executingCompany(statements);
                    }
                    if (stProductModel.getDataCount() % BATCH_SIZE == 0) {
                        executingProductModel(statements);
                    }
                    if (stContracts.getDataCount() % BATCH_SIZE == 0) {
                        executingContract(statements);
                    }
                    if (stOrders.getDataCount() % BATCH_SIZE == 0) {
                        executingOrder(statements);
                    }

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
            System.out.println(InsertMethods.orderID);
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
        System.out.println("Cost : " + (end - start) / 1000L + " seconds");
        System.out.println("Data count : " + InsertMethods.orderID);
        System.out.println("Loading speed : " + (InsertMethods.orderID * 1000L) / (end - start) + " records/s");

    }


    public static void executingSupplyCenter(StatementPreparer[] statements) {
        StatementPreparer stSupplyCenter = statements[0];

        System.out.println("SupplyCenter execute " + stSupplyCenter.getDataCount());
        stSupplyCenter.execute();
    }

    public static void executingProduct(StatementPreparer[] statements) {
        StatementPreparer stProduct = statements[1];


        System.out.println("Product execute " + stProduct.getDataCount());
        stProduct.execute();
    }

    public static void executingSalesman(StatementPreparer[] statements) {
        StatementPreparer stSalesman = statements[2];

        StatementPreparer stSupplyCenter = statements[0];
        if (stSupplyCenter.getDataCount() > 0) {
            executingSupplyCenter(statements);
        }


        System.out.println("Salesman execute " + stSalesman.getDataCount());
        stSalesman.execute();
    }

    public static void executingCompany(StatementPreparer[] statements) {
        StatementPreparer stCompany = statements[3];

        StatementPreparer stSupplyCenter = statements[0];
        if (stSupplyCenter.getDataCount() > 0) {
            executingSupplyCenter(statements);
        }


        System.out.println("Company execute " + stCompany.getDataCount());
        stCompany.execute();
    }

    public static void executingProductModel(StatementPreparer[] statements) {
        StatementPreparer stProductModel = statements[4];


        StatementPreparer stProduct = statements[1];
        if (stProduct.getDataCount() > 0) {
            executingProduct(statements);
        }


        System.out.println("ProductModel execute " + stProductModel.getDataCount());
        stProductModel.execute();
    }

    public static void executingContract(StatementPreparer[] statements) {
        StatementPreparer stContracts = statements[5];

        StatementPreparer stCompany = statements[3];
        if (stCompany.getDataCount() > 0) {
            executingCompany(statements);
        }


        System.out.println("ProductModel execute " + stContracts.getDataCount());
        stContracts.execute();
    }

    public static void executingOrder(StatementPreparer[] statements) {
        StatementPreparer stOrders = statements[6];

        StatementPreparer stSalesman = statements[2];
        StatementPreparer stProductModel = statements[4];
        StatementPreparer stContracts = statements[5];

        if (stContracts.getDataCount() > 0) {
            executingContract(statements);
        }

        if (stProductModel.getDataCount() > 0) {
            executingProductModel(statements);
        }

        if (stSalesman.getDataCount() > 0) {
            executingSalesman(statements);
        }

        System.out.println("Orders execute " + stOrders.getDataCount());
        stOrders.execute();
    }


}