package myz.myDBLoading2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenID {
    private Integer currentSupplyCenterID = 1;
    private Integer currentSalesmanID = 1;
    private Integer currentProductID = 1;
    private Integer currentCompanyID = 1;
    private Integer currentModelID = 1;
    private Integer currentContractID = 1;


    public HashMap<String, Integer> supplyCenters = new HashMap<>();
    public HashMap<String, Integer> products = new HashMap<>();
    public HashMap<String, Integer> salesmans = new HashMap<>();
    public HashMap<String, Integer> Companys = new HashMap<>();
    public HashMap<String, Integer> productModels = new HashMap<>();
    public HashMap<String, Integer> Contracts = new HashMap<>();

    private List<HashMap<String, Integer>> hashMaps = new ArrayList<>();

    {
        hashMaps.add(supplyCenters);
        hashMaps.add(products);
        hashMaps.add(salesmans);
        hashMaps.add(Companys);
        hashMaps.add(productModels);
        hashMaps.add(Contracts);};

    private String[] pks = new String[]{
            null,//supplyCenterName,
            null,//salesmanNumber,
            null,//productCode,
            null,//clientEnterprise,
            null,//productModel,
            null//contractNumber
    };


    public void genID(String[] values){
        if (values.length != 21){
            System.err.println("Wrong Array");
            return;
        }
//        supplyCenterName
        pks[0] = values[2];
//        salesmanNumber
        pks[1] = values[16];
//        productCode
        pks[2]=values[6];
//        clientEnterprise
        pks[3]=values[1];
//        productModel
        pks[4] =values[8];
//        contractNumber
        pks[5] =values[0];



        if (!hashMaps.get(0).containsKey(pks[0])) {
            hashMaps.get(0).put(pks[0], currentSupplyCenterID);
            currentSupplyCenterID++;
        }
        if (!hashMaps.get(1).containsKey(pks[1])) {
            hashMaps.get(1).put(pks[1], currentSalesmanID);
            currentSalesmanID++;
        }
        if (!hashMaps.get(2).containsKey(pks[2])) {
            hashMaps.get(2).put(pks[2], currentProductID);
            currentProductID++;
        }
        if (!hashMaps.get(3).containsKey(pks[3])) {
            hashMaps.get(3).put(pks[3], currentCompanyID);
            currentCompanyID++;
        }
        if (!hashMaps.get(4).containsKey(pks[4])) {
            hashMaps.get(4).put(pks[4], currentModelID);
            currentModelID++;
        }
        if (!hashMaps.get(5).containsKey(pks[5])) {
            hashMaps.get(5).put(pks[5], currentContractID);
            currentContractID++;
        }

    }

    public int[] getID(){
        return new int[]{
                hashMaps.get(0).get(pks[0]),
                hashMaps.get(1).get(pks[1]),
                hashMaps.get(2).get(pks[2]),
                hashMaps.get(3).get(pks[3]),
                hashMaps.get(4).get(pks[4]),
                hashMaps.get(5).get(pks[5]),
        };
    }

    public void alterGender(String[] values){

        switch (values[17]){
            case "Female":
                values[17] = "F";
                break;
            case "Male":
                values[17] = "M";
                break;
            default:
                values[17] = "U";
        }

    }












    public int getCurrentContractID() {
        return currentContractID;
    }

    public void setCurrentContractID(int currentContractID) {
        this.currentContractID = currentContractID;
    }

    public int getCurrentModelID() {
        return currentModelID;
    }

    public void setCurrentModelID(int currentModelID) {
        this.currentModelID = currentModelID;
    }

    public int getCurrentCompanyID() {
        return currentCompanyID;
    }

    public void setCurrentCompanyID(int currentCompanyID) {
        this.currentCompanyID = currentCompanyID;
    }

    public int getCurrentProductID() {
        return currentProductID;
    }

    public void setCurrentProductID(int currentProductID) {
        this.currentProductID = currentProductID;
    }

    public int getCurrentSalesmanID() {
        return currentSalesmanID;
    }

    public void setCurrentSalesmanID(int currentSalesmanID) {
        this.currentSalesmanID = currentSalesmanID;
    }

    public int getCurrentSupplyCenterID() {
        return currentSupplyCenterID;
    }

    public void setCurrentSupplyCenterID(int currentSupplyCenterID) {
        this.currentSupplyCenterID = currentSupplyCenterID;
    }


}
