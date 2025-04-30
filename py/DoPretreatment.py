import pandas as pd
import time
import psycopg2
from SqlSentences import SQLSentences, SQLSentencesManager
from DbConnection import DBConnection
from newInsertMe import InsertMethods
from io import StringIO

batch = 1000

db = DBConnection()
config = db.load_properties()
csv_file = '../resources/output25S.csv'

df = pd.read_csv(csv_file, dtype=str, header=None, skiprows=1)
df = df.iloc[:, :-1]  # 移除最后一列
df = df.replace('', None).replace('nan', None)  # 处理空值

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

    # 使用 COPY 命令插入数据
    def copy_to_db(data, table_name, columns, cursor, con):
        if not data:
            return
        df = pd.DataFrame(data, columns=columns)
        buffer = StringIO()
        df.to_csv(buffer, index=False, header=False, na_rep='\\N')
        buffer.seek(0)
        cursor.copy_expert(f"COPY {table_name} ({','.join(columns)}) FROM STDIN WITH (FORMAT CSV, NULL '\\N')", buffer)
        con.commit()

    copy_to_db(
        supply_center,
        'supply_centers',
        ['supply_center_id', 'supply_center', 'director'],
        cursor,
        db.get_connection()
    )
    copy_to_db(
        product,
        'product',
        ['product_id', 'product_code', 'product_name'],
        cursor,
        db.get_connection()
    )
    copy_to_db(
        company,
        'company',
        ['company_id', 'supply_center_id', 'company_name', 'country', 'city', 'industry'],
        cursor,
        db.get_connection()
    )
    copy_to_db(
        salesman,
        'salesman',
        ['salesman_id', 'supply_center_id', 'salesman_number', 'salesman_name', 'gender', 'age', 'mobile_phone'],
        cursor,
        db.get_connection()
    )
    copy_to_db(
        product_model,
        'product_model',
        ['model_id', 'product_id', 'product_model', 'unit_price'],
        cursor,
        db.get_connection()
    )
    copy_to_db(
        contracts,
        'contracts',
        ['contract_id', 'company_id', 'contract_number', 'contract_date'],
        cursor,
        db.get_connection()
    )
    copy_to_db(
        orders,
        'orders',
        ['order_id', 'contract_id', 'model_id', 'salesman_id', 'quantity', 'estimated_delivery_date', 'lodgement_date'],
        cursor,
        db.get_connection()
    )

    cursor.execute(sql_manager.sql_sentences_map[SQLSentences.ADD_CONSTRAINT])
    db.get_connection().commit()

except psycopg2.Error as e:
    print(f"Database error at record {im._orderId_}: {e}")
    raise
except ValueError as e:
    print(f"Value error at record {im._orderId_}: {e}")
    raise
except Exception as e:
    print(f"Unknown error at record {im._orderId_}: {e}")
    raise
finally:
    db.close_db()

end = time.perf_counter()
print(f"Time cost: {end - start} s")
print(f"Successfully load {cnt} data")
print(f"Loading speed: {cnt / (end - start)} records/s")