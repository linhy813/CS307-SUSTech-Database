import string
from typing import List

import PrepareStatement
from DataType import DataType


class InsertMethods:
    _supplyCenterId_ = 0
    _companyId_ = 0
    _orderId_ = 0
    _contractId_ = 0
    _salesmanId_ = 0
    _productModelId_ = 0
    _productId_ = 0

    _supplyCenter_dict_ = {}
    _company_dict_ = {}
    _order_dict_ = {}
    _contract_dict_ = {}
    _salesman_dict_ = {}
    _product_dict_ = {}
    _productModel_dict_ = {}

    def __init__(self):
        self.supplyCenter = None
        self.salesmanNumber = None
        self.productCode = None
        self.companyName = None
        self.productModel = None
        self.contractNumber = None

    def setValues(self, values):
        if not len(values) == 20:
            raise RuntimeError("Invalid input")

        self.supplyCenter = values[2]
        self.salesmanNumber = values[16]
        self.productCode = values[6]
        self.companyName = values[1]
        self.productModel = values[8]
        self.contractNumber = values[0]

    def insertSupplyCenter(self, line: List[string], ps: PrepareStatement):
        if self.supplyCenter in self._supplyCenter_dict_:
            return

        self._supplyCenterId_ += 1
        self._supplyCenter_dict_[self.supplyCenter] = self._supplyCenterId_

        director = line[14]
        ps.insertData(f"{self._supplyCenterId_}", DataType.INT, 1)
        ps.insertData(self.supplyCenter, DataType.STRING, 2)
        ps.insertData(director, DataType.STRING, 3)
        ps.addIntoList()

    def insertCompany(self, line: List[string], ps: PrepareStatement):
        if self.companyName in self._company_dict_:
            return

        self._companyId_ += 1
        self._company_dict_[self.companyName] = self._companyId_

        country = line[3]
        city = line[4]
        industry = line[5]

        ps.insertData(f"{self._companyId_}", DataType.INT, 1)
        ps.insertData(f"{self._supplyCenter_dict_[self.supplyCenter]}", DataType.INT, 2)
        ps.insertData(self.companyName, DataType.STRING, 3)
        ps.insertData(country, DataType.STRING, 4)
        ps.insertData(city, DataType.STRING, 5)
        ps.insertData(industry, DataType.STRING, 6)
        ps.addIntoList()

    def insertContract(self, line: List[string], ps: PrepareStatement):
        if self.contractNumber in self._contract_dict_:
            return

        self._contractId_ += 1
        self._contract_dict_[self.contractNumber] = self._contractId_

        contractDate = line[11]

        ps.insertData(f"{self._contractId_}", DataType.INT, 1)
        ps.insertData(f"{self._company_dict_[self.companyName]}", DataType.INT, 2)
        ps.insertData(self.contractNumber, DataType.STRING, 3)
        ps.insertData(contractDate, DataType.DATE, 4)
        ps.addIntoList()

    def insertSalesman(self, line: List[string], ps: PrepareStatement):
        if self.salesmanNumber in self._salesman_dict_:
            return

        self._salesmanId_ += 1
        self._salesman_dict_[self.salesmanNumber] = self._salesmanId_

        salesmanName = line[15]
        gender = line[17]
        match gender:
            case 'Male':
                gender = 'M'

            case 'Female':
                gender = 'F'

            case 'Unknown':
                gender = 'U'

        age = line[18]
        mobile_phone = line[19]

        ps.insertData(f"{self._salesmanId_}", DataType.INT, 1)
        ps.insertData(f"{self._supplyCenter_dict_[self.supplyCenter]}", DataType.INT, 2)
        ps.insertData(self.salesmanNumber, DataType.STRING, 3)
        ps.insertData(salesmanName, DataType.STRING, 4)
        ps.insertData(gender, DataType.STRING, 5)
        ps.insertData(age, DataType.INT, 6)
        ps.insertData(mobile_phone, DataType.STRING, 7)
        ps.addIntoList()

    def insertOrder(self, line: List[string], ps: PrepareStatement):
        self._orderId_ += 1

        quantity = line[10]
        estimatedDeliveryDate = line[12]
        lodgementDate = line[13]

        ps.insertData(f"{self._orderId_}", DataType.INT, 1)
        ps.insertData(f"{self._contract_dict_[self.contractNumber]}", DataType.INT, 2)
        ps.insertData(f"{self._productModel_dict_[self.productModel]}", DataType.INT, 3)
        ps.insertData(f"{self._salesman_dict_[self.salesmanNumber]}", DataType.INT, 4)
        ps.insertData(quantity, DataType.INT, 5)
        ps.insertData(estimatedDeliveryDate, DataType.DATE, 6)
        ps.insertData(lodgementDate, DataType.DATE, 7)
        ps.addIntoList()

    def insertProductModel(self, line: List[string], ps: PrepareStatement):
        if self.productModel in self._productModel_dict_:
            return

        self._productModelId_ += 1
        self._productModel_dict_[self.productModel] = self._productModelId_

        unitPrice = line[9]

        ps.insertData(f"{self._productModelId_}", DataType.INT, 1)
        ps.insertData(f"{self._product_dict_[self.productCode]}", DataType.INT, 2)
        ps.insertData(self.productModel, DataType.STRING, 3)
        ps.insertData(unitPrice, DataType.INT, 4)
        ps.addIntoList()

    def insertProduct(self, line: List[string], ps: PrepareStatement):
        if self.productCode in self._product_dict_:
            return

        self._productId_ += 1
        self._product_dict_[self.productCode] = self._productId_

        product_name = line[7]
        ps.insertData(f"{self._productId_}", DataType.INT, 1)
        ps.insertData(self.productCode, DataType.STRING, 2)
        ps.insertData(product_name, DataType.STRING, 3)
        ps.addIntoList()
