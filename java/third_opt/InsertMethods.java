package myz.myDBLoading;

import java.util.HashMap;

public class InsertMethods {

    // 提供id
    public static int supplyCenterID = 0;
    public static int salesmanID = 0;
    public static int productID = 0;
    public static int companyID = 0;
    public static int modelID = 0;
    public static int contractID = 0;
    public static int orderID = 0;



    //把提取数组主键内容单独做一个方法，再多加点静态变量
    public static String supplyCenterName;
    public static String salesmanNumber;
    public static String productCode;
    public static String clientEnterprise;
    public static String productModel;
    public static String contractNumber;



    public static void getPK(String[] values){
        if (values.length != 21){
            System.err.println("Wrong Array");
            return;
        }
        supplyCenterName =values[2];
        salesmanNumber =values[16];
        productCode =values[6];
        clientEnterprise=values[1];
        productModel =values[8];
        contractNumber =values[0];

    }




    //用来去重的集合
    private static HashMap<String, Integer> supplyCenters = new HashMap<>();
    public static void insertSupplyCenter(String[] line, StatementPreparer sp){

        //判断是否重复
        if (supplyCenters.containsKey(supplyCenterName)) {
             return; }

        String directorName = line[14];
        supplyCenterID++;
        //如果不重复
        //填数据
        sp.fillOneData(""+supplyCenterID, UsedDataType.INT, 1);
        sp.fillOneData(supplyCenterName, UsedDataType.STRING,2);
        sp.fillOneData(directorName,UsedDataType.STRING,3);
        //更新
        supplyCenters.put(supplyCenterName,supplyCenterID);
        sp.update();

    }


    //用来去重的集合
    private static HashMap<String, Integer> products = new HashMap<>();
    public static void insertProduct(String[] line, StatementPreparer sp){

        //判断是否重复
        if (products.containsKey(productCode)) {
            return; }

        productID++;
        String product_name = line[7];
        //如果不重复
        //填数据
        sp.fillOneData(""+productID, UsedDataType.INT, 1);
        sp.fillOneData(productCode, UsedDataType.STRING,2);
        sp.fillOneData(product_name,UsedDataType.STRING,3);
        //更新
        products.put(productCode,productID);
        sp.update();

    }


    //用来去重的集合
    private static HashMap<String, Integer> salesmanNumbers = new HashMap<>();
    public static void insertSalesman(String[] line, StatementPreparer sp){

//        String salesmanNumber =line[16];
        //判断是否重复
        if (salesmanNumbers.containsKey(salesmanNumber)) {
             return; }

        salesmanID++;
        //处理数据
        String salesmanName = line[15];
        String gender = line[17];
        switch (gender){
            case "Female":
                gender = "F";
                break;
            case "Male":
                gender = "M";
                break;
            default:
                gender = "U";
        }
        String age = line[18];
        String mobile_phone = line[19];


        //如果不重复
        //填数据
        sp.fillOneData(""+salesmanID, UsedDataType.INT, 1);
        sp.fillOneData(""+supplyCenters.get(supplyCenterName), UsedDataType.INT, 2);
        sp.fillOneData(salesmanNumber, UsedDataType.INT, 3);
        sp.fillOneData(salesmanName, UsedDataType.STRING,4);
        sp.fillOneData(gender,UsedDataType.STRING,5);
        sp.fillOneData(age, UsedDataType.INT, 6);
        sp.fillOneData(mobile_phone, UsedDataType.STRING, 7);
        //更新
        salesmanNumbers.put(salesmanNumber,salesmanID);
//        salesmanID++;
        sp.update();

    }


    //用来去重的集合
    private static HashMap<String, Integer> companys = new HashMap<>();
    public static void insertCompany(String[] line, StatementPreparer sp){

        //判断是否重复
        if (companys.containsKey(clientEnterprise)) {
            return; }

        companyID++;
        String country = line[3];
        String city = line[4];
        String industry = line[5];

        //如果不重复
        //填数据
        sp.fillOneData(""+companyID, UsedDataType.INT, 1);
        sp.fillOneData(""+supplyCenters.get(supplyCenterName), UsedDataType.INT, 2);
        sp.fillOneData(clientEnterprise, UsedDataType.STRING, 3);
        sp.fillOneData(country, UsedDataType.STRING,4);
        sp.fillOneData(city,UsedDataType.STRING,5);
        sp.fillOneData(industry,UsedDataType.STRING,6);
        //更新
        companys.put(clientEnterprise,companyID);
        sp.update();

    }

    //用来去重的集合
    private static HashMap<String, Integer> productModels = new HashMap<>();
    public static void insertProductModel(String[] line, StatementPreparer sp){

        //判断是否重复
        if (productModels.containsKey(productModel)) {
            return; }

        modelID++;
        String unitPrice = line[9];

        //如果不重复
        sp.fillOneData(""+modelID, UsedDataType.INT, 1);
        sp.fillOneData(""+products.get(productCode), UsedDataType.INT,2);
        sp.fillOneData(productModel,UsedDataType.STRING,3);
        sp.fillOneData(unitPrice,UsedDataType.INT,4);
        //更新
        productModels.put(productModel,modelID);
        sp.update();

    }

    //用来去重的集合
    private static HashMap<String, Integer> contracts = new HashMap<>();
    public static void insertContract(String[] line, StatementPreparer sp){

        //判断是否重复
        if (contracts.containsKey(contractNumber)) {
            return; }


        contractID++;
        String contractDate =line[11];

        //如果不重复
        sp.fillOneData(""+contractID, UsedDataType.INT, 1);
        sp.fillOneData(contractNumber,UsedDataType.STRING,2);
        sp.fillOneData(""+companys.get(clientEnterprise), UsedDataType.INT,3);
        sp.fillOneData(contractDate,UsedDataType.DATE,4);
        //更新
        contracts.put(contractNumber,contractID);
        sp.update();

    }



    public static void insertOrders(String[] line, StatementPreparer sp){

        String quantity = line[10];
        String estimatedDeliveryDate = line[12];
        String lodgementDate = line[13];



        //如果不重复
        sp.fillOneData(""+orderID, UsedDataType.INT, 1);
        sp.fillOneData(""+contracts.get(contractNumber), UsedDataType.INT, 2);
        sp.fillOneData(""+productModels.get(productModel), UsedDataType.INT, 3);
        sp.fillOneData(""+salesmanNumbers.get(salesmanNumber), UsedDataType.INT, 4);
        sp.fillOneData(quantity,UsedDataType.INT,5);
        sp.fillOneData(estimatedDeliveryDate, UsedDataType.DATE,6);
        sp.fillOneData(lodgementDate,UsedDataType.DATE,7);
        //更新
        orderID++;
        sp.update();

    }



    //

    //

}
