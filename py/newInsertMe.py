from typing import List

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
        if len(values) != 20:
            raise RuntimeError("Invalid input: expected 20 values")

        self.supplyCenter = values[2]
        self.salesmanNumber = values[16]
        self.productCode = values[6]
        self.companyName = values[1]
        self.productModel = values[8]
        self.contractNumber = values[0]

    def insertSupplyCenter(self, line: List[str], data_list: List):
        if self.supplyCenter in self._supplyCenter_dict_:
            return

        self._supplyCenterId_ += 1
        self._supplyCenter_dict_[self.supplyCenter] = self._supplyCenterId_

        director = line[14]
        data_list.append([self._supplyCenterId_, self.supplyCenter, director])

    def insertCompany(self, line: List[str], data_list: List):
        if self.companyName in self._company_dict_:
            return

        self._companyId_ += 1
        self._company_dict_[self.companyName] = self._companyId_

        country = line[3]
        city = line[4]
        industry = line[5]
        data_list.append([
            self._companyId_,
            self._supplyCenter_dict_.get(self.supplyCenter),
            self.companyName,
            country,
            city,
            industry
        ])

    def insertContract(self, line: List[str], data_list: List):
        if self.contractNumber in self._contract_dict_:
            return

        self._contractId_ += 1
        self._contract_dict_[self.contractNumber] = self._contractId_

        contractDate = line[11]
        data_list.append([
            self._contractId_,
            self._company_dict_.get(self.companyName),
            self.contractNumber,
            contractDate
        ])

    def insertSalesman(self, line: List[str], data_list: List):
        if self.salesmanNumber in self._salesman_dict_:
            return

        self._salesmanId_ += 1
        self._salesman_dict_[self.salesmanNumber] = self._salesmanId_

        salesmanName = line[15]
        gender = line[17]
        gender = {'Male': 'M', 'Female': 'F', 'Unknown': 'U'}.get(gender, gender)
        age = line[18]
        mobile_phone = line[19]
        data_list.append([
            self._salesmanId_,
            self._supplyCenter_dict_.get(self.supplyCenter),
            self.salesmanNumber,
            salesmanName,
            gender,
            age,
            mobile_phone
        ])

    def insertOrder(self, line: List[str], data_list: List):
        self._orderId_ += 1

        quantity = line[10]
        estimatedDeliveryDate = line[12]
        lodgementDate = line[13]
        data_list.append([
            self._orderId_,
            self._contract_dict_.get(self.contractNumber),
            self._productModel_dict_.get(self.productModel),
            self._salesman_dict_.get(self.salesmanNumber),
            quantity,
            estimatedDeliveryDate,
            lodgementDate
        ])

    def insertProductModel(self, line: List[str], data_list: List):
        if self.productModel in self._productModel_dict_:
            return

        self._productModelId_ += 1
        self._productModel_dict_[self.productModel] = self._productModelId_

        unitPrice = line[9]
        data_list.append([
            self._productModelId_,
            self._product_dict_.get(self.productCode),
            self.productModel,
            unitPrice
        ])

    def insertProduct(self, line: List[str], data_list: List):
        if self.productCode in self._product_dict_:
            return

        self._productId_ += 1
        self._product_dict_[self.productCode] = self._productId_

        product_name = line[7]
        data_list.append([self._productId_, self.productCode, product_name])