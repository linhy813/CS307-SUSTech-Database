import re

import numpy as np

from DbConnection import DBConnection
import time
import psycopg2
from SqlSentences import SQLSentences, SQLSentencesManager
from newInsertMe import InsertMethods
import pandas as pd
import glob

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
sql_manager = SQLSentencesManager()

try:
    cursor = db.get_stmt()
    cursor.execute(sql_manager.sql_sentences_map[SQLSentences.CLEAN_ALL])
    db.get_connection().commit()

    cursor.execute(sql_manager.sql_sentences_map[SQLSentences.CREATE_ALL_WITHOUT_CONSTRAINT])
    db.get_connection().commit()

    supply_center = []
    product = []
    company = []
    salesman = []
    product_model = []
    contracts = []
    orders = []

    for row in df.itertuples(index=False):
        values = list(row)

        cnt += 1
        im.setValues(values)
        im.insertSupplyCenter(values, supply_center)
        im.insertProduct(values, product)
        im.insertCompany(values, company)
        im.insertSalesman(values, salesman)
        im.insertProductModel(values, product_model)
        im.insertContract(values, contracts)
        im.insertOrder(values, orders)

    cursor.executemany(sql_manager.sql_sentences_map[SQLSentences.INSERT_SUPPLY_CENTER], supply_center)
    db.get_connection().commit()
    cursor.executemany(sql_manager.sql_sentences_map[SQLSentences.INSERT_PRODUCT], product)
    db.get_connection().commit()
    cursor.executemany(sql_manager.sql_sentences_map[SQLSentences.INSERT_COMPANY], company)
    db.get_connection().commit()
    cursor.executemany(sql_manager.sql_sentences_map[SQLSentences.INSERT_SALESMAN], salesman)
    db.get_connection().commit()
    cursor.executemany(sql_manager.sql_sentences_map[SQLSentences.INSERT_PRODUCT_MODEL], product_model)
    db.get_connection().commit()
    cursor.executemany(sql_manager.sql_sentences_map[SQLSentences.INSERT_CONTRACTS], contracts)
    db.get_connection().commit()
    cursor.executemany(sql_manager.sql_sentences_map[SQLSentences.INSERT_ORDERS], orders)
    db.get_connection().commit()

    cursor.execute(sql_manager.sql_sentences_map[SQLSentences.ADD_CONSTRAINT])
    db.get_connection().commit()

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
