package myz.myDBLoading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InsertMethods {

    // 提供id
    private static int supplyCenterID = 1;
    private static int productID = 1;
    private static int companyID = 1;
    private static int modelID = 1;
    private static int contractID = 1;
    private static int orderID = 1;
    public static int salesmanID = 1;


    //把提取数组内容单独做一个方法，再多加点静态变量
    //ID++单独做一个方法

    //用来去重的集合
    private static HashMap<String, Integer> supplyCenters = new HashMap<String, Integer>();
    public static void insertSupplyCenter(String[] line, StatementPreparer sp){

        String supplyCenterName =line[2];
        //判断是否重复
        if (supplyCenters.containsKey(supplyCenterName)) {
             return; }

        String directorName = line[14];
        //如果不重复
        //填数据
        sp.fillOneData(""+supplyCenterID, UsedDataType.INT, 1);
        sp.fillOneData(supplyCenterName, UsedDataType.STRING,2);
        sp.fillOneData(directorName,UsedDataType.STRING,3);
        //更新
        supplyCenters.put(supplyCenterName,supplyCenterID);
        supplyCenterID++;
        sp.update();

    }


    //用来去重的集合
    private static HashMap<String, Integer> salesmanNumbers = new HashMap<>();
    public static void insertSalesman(String[] line, StatementPreparer sp){

        String salesmanNumber =line[16];
        //判断是否重复
        if (salesmanNumbers.containsKey(salesmanNumber)) {
            return; }

        //处理数据
        String salesmanName = line[15];
        String supplyCenterName =line[2];
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
        salesmanID++;
        sp.update();

    }


    //用来去重的集合
    private static HashMap<String, Integer> products = new HashMap();
    public static void insertProduct(String[] line, StatementPreparer sp){

        String productCode =line[6];
        //判断是否重复
        if (products.containsKey(productCode)) {
            return; }

        String product_name = line[7];
        //如果不重复
        //填数据
        sp.fillOneData(""+productID, UsedDataType.INT, 1);
        sp.fillOneData(productCode, UsedDataType.STRING,2);
        sp.fillOneData(product_name,UsedDataType.STRING,3);
        //更新
        products.put(productCode,productID);
        productID++;
        sp.update();

    }

    //用来去重的集合
    private static HashMap<String, Integer> companys = new HashMap<>();
    public static void insertCompany(String[] line, StatementPreparer sp){

        String clientEnterprise=line[1];
        //判断是否重复
        if (companys.containsKey(clientEnterprise)) {
            return; }

        String country = line[3];
        String city = line[4];
        String industry = line[5];

        String supplyCenterName =line[2];
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
        companyID++;
        sp.update();

    }

    //用来去重的集合
    private static HashMap<String, Integer> productModels = new HashMap<String, Integer>();
    public static void insertProductModel(String[] line, StatementPreparer sp){

        String productModel =line[8];
        //判断是否重复
        if (productModels.containsKey(productModel)) {
            return; }

        String unitPrice = line[9];

        String productCode =line[6];
        //如果不重复
        sp.fillOneData(""+modelID, UsedDataType.INT, 1);
        sp.fillOneData(""+products.get(productCode), UsedDataType.INT,2);
        sp.fillOneData(productModel,UsedDataType.STRING,3);
        sp.fillOneData(unitPrice,UsedDataType.INT,4);
        //更新
        productModels.put(productModel,modelID);
        modelID++;
        sp.update();

    }

    //用来去重的集合
    private static HashMap<String, Integer> contracts = new HashMap<String, Integer>();
    public static void insertContract(String[] line, StatementPreparer sp){

        String contractNumber =line[0];
        //判断是否重复
        if (contracts.containsKey(contractNumber)) {
            return; }


        String contractDate =line[11];

        String clientEnterprise=line[1];
        //如果不重复
        sp.fillOneData(""+contractID, UsedDataType.INT, 1);
        sp.fillOneData(contractNumber,UsedDataType.STRING,2);
        sp.fillOneData(""+companys.get(clientEnterprise), UsedDataType.INT,3);
        sp.fillOneData(contractDate,UsedDataType.DATE,4);
        //更新
        contracts.put(contractNumber,contractID);
        contractID++;
        sp.update();

    }



    public static void insertOrders(String[] line, StatementPreparer sp){

        String quantity = line[10];
        String estimatedDeliveryDate = line[12];
        String lodgementDate = line[13];

        String contractNumber =line[0];
        String productModel =line[8];
        String salesmanNumber =line[16];

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
    public static int getSupplyCenterID() {
        return supplyCenterID;
    }

    public static void setSupplyCenterID(int supplyCenterID) {
        InsertMethods.supplyCenterID = supplyCenterID;
    }

    public static int getProductID() {
        return productID;
    }

    public static void setProductID(int productID) {
        InsertMethods.productID = productID;
    }

    public static int getCompanyID() {
        return companyID;
    }

    public static void setCompanyID(int companyID) {
        InsertMethods.companyID = companyID;
    }

    public static int getModelID() {
        return modelID;
    }

    public static void setModelID(int modelID) {
        InsertMethods.modelID = modelID;
    }

    public static int getContractID() {
        return contractID;
    }

    public static void setContractID(int contractID) {
        InsertMethods.contractID = contractID;
    }

    public static int getOrderID() {
        return orderID;
    }

    public static void setOrderID(int orderID) {
        InsertMethods.orderID = orderID;
    }
}
