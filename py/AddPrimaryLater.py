import numpy as np

from PrepareStatement import PrepareStatement
from DbConnection import DBConnection
import time
import psycopg2
from SqlSentences import SQLSentences
from InsertMethods import InsertMethods
import pandas as pd

batch = 1000

db = DBConnection()
config = db.load_properties()
csv_file = '../resources/output25S.csv'

df = pd.read_csv(csv_file, dtype=str, header=None, skiprows=1)
df = df.iloc[:, :-1]  # 移除最后一列
df = df.replace(['', 'nan', 'NaN', 'NAN', np.nan], None)  # 处理空值、'nan' 变体和 np.nan

cnt = 0
start = time.perf_counter()
db.open_db(config)

im = InsertMethods()

try:
    cleanAll = PrepareStatement(db, SQLSentences.CLEAN_ALL)
    cleanAll.cursor.execute(cleanAll.sql)
    cleanAll.con.commit()

    createAllWithoutConstraint = PrepareStatement(db, SQLSentences.CREATE_ALL_WITHOUT_CONSTRAINT)
    createAllWithoutConstraint.cursor.execute(createAllWithoutConstraint.sql)
    createAllWithoutConstraint.con.commit()

    psSupplyCenter = PrepareStatement(db, SQLSentences.INSERT_SUPPLY_CENTER)
    psSalesman = PrepareStatement(db, SQLSentences.INSERT_SALESMAN)
    psProduct = PrepareStatement(db, SQLSentences.INSERT_PRODUCT)
    psProductModel = PrepareStatement(db, SQLSentences.INSERT_PRODUCT_MODEL)
    psContract = PrepareStatement(db, SQLSentences.INSERT_CONTRACTS)
    psOrders = PrepareStatement(db, SQLSentences.INSERT_ORDERS)
    psCompany = PrepareStatement(db, SQLSentences.INSERT_COMPANY)

    for row in df.itertuples(index=False):
        values = list(row)
        cnt += 1
        im.setValues(values)
        im.insertSupplyCenter(values, psSupplyCenter)
        im.insertProduct(values, psProduct)
        im.insertCompany(values, psCompany)
        im.insertSalesman(values, psSalesman)
        im.insertProductModel(values, psProductModel)
        im.insertContract(values, psContract)
        im.insertOrder(values, psOrders)

        if im._supplyCenterId_ % batch == 0 and psSupplyCenter.params:
            psSupplyCenter.cursor.executemany(psSupplyCenter.sql, psSupplyCenter.params)
            psSupplyCenter.con.commit()
            psSupplyCenter.params.clear()
        if im._productId_ % batch == 0 and psProduct.params:
            psProduct.cursor.executemany(psProduct.sql, psProduct.params)
            psProduct.con.commit()
            psProduct.params.clear()
        if im._salesmanId_ % batch == 0 and psSalesman.params:
            psSalesman.cursor.executemany(psSalesman.sql, psSalesman.params)
            psSalesman.con.commit()
            psSalesman.params.clear()
        if im._productModelId_ % batch == 0 and psProductModel.params:
            psProductModel.cursor.executemany(psProductModel.sql, psProductModel.params)
            psProductModel.con.commit()
            psProductModel.params.clear()
        if im._companyId_ % batch == 0 and psCompany.params:
            psCompany.cursor.executemany(psCompany.sql, psCompany.params)
            psCompany.con.commit()
            psCompany.params.clear()
        if im._contractId_ % batch == 0 and psContract.params:
            psContract.cursor.executemany(psContract.sql, psContract.params)
            psContract.con.commit()
            psContract.params.clear()
        if im._orderId_ % batch == 0 and psOrders.params:
            psOrders.cursor.executemany(psOrders.sql, psOrders.params)
            psOrders.con.commit()
            psOrders.params.clear()

    if psSupplyCenter.params:
        psSupplyCenter.cursor.executemany(psSupplyCenter.sql, psSupplyCenter.params)
        psSupplyCenter.con.commit()
        psSupplyCenter.params.clear()
    if psProduct.params:
        psProduct.cursor.executemany(psProduct.sql, psProduct.params)
        psProduct.con.commit()
        psProduct.params.clear()
    if psSalesman.params:
        psSalesman.cursor.executemany(psSalesman.sql, psSalesman.params)
        psSalesman.con.commit()
        psSalesman.params.clear()
    if psProductModel.params:
        psProductModel.cursor.executemany(psProductModel.sql, psProductModel.params)
        psProductModel.con.commit()
        psProductModel.params.clear()
    if psCompany.params:
        psCompany.cursor.executemany(psCompany.sql, psCompany.params)
        psCompany.con.commit()
        psCompany.params.clear()
    if psContract.params:
        psContract.cursor.executemany(psContract.sql, psContract.params)
        psContract.con.commit()
        psContract.params.clear()
    if psOrders.params:
        psOrders.cursor.executemany(psOrders.sql, psOrders.params)
        psOrders.con.commit()
        psOrders.params.clear()

    addConstraint = PrepareStatement(db, SQLSentences.ADD_CONSTRAINT)
    addConstraint.cursor.execute(addConstraint.sql)
    addConstraint.con.commit()

except psycopg2.Error as e:
    print(f"数据库错误发生在第 {im._orderId_} 条记录：{e}")
    raise
except ValueError as e:
    print(f"值错误发生在第 {im._orderId_} 条记录：{e}")
    raise
except Exception as e:
    print(f"未知错误发生在第 {im._orderId_} 条记录：{e}")
    raise
finally:
    db.close_db()

end = time.perf_counter()
print(f"Time cost: {end - start} s")
print(f"Successfully load {cnt} data")
print(f"Loading speed：{(cnt) / (end - start)} records/s")
