from enum import Enum
from typing import Dict


class SQLSentences(Enum):
    INSERT_CONTRACTS = "INSERT_CONTRACTS"
    INSERT_ORDERS = "INSERT_ORDERS"
    INSERT_PRODUCT_MODEL = "INSERT_PRODUCT_MODEL"
    INSERT_PRODUCT = "INSERT_PRODUCT"
    INSERT_SALESMAN = "INSERT_SALESMAN"
    INSERT_SUPPLY_CENTER = "INSERT_SUPPLY_CENTER"
    INSERT_COMPANY = "INSERT_COMPANY"
    CLEAN_ALL = "CLEAN_ALL"
    CREATE_ALL = "CREATE_ALL"
    CREATE_ALL_WITHOUT_CONSTRAINT = "CREATE_ALL_WITHOUT_CONSTRAINT"
    ADD_CONSTRAINT = "ADD_CONSTRAINT"
    DISABLE_CONSTRAINT = "DISABLE_CONSTRAINT"


class SQLSentencesManager:
    INSERT_CONTRACTS = """
        INSERT INTO contracts (contract_id, company_id, contract_number, contract_date)
        VALUES (%s, %s, %s, %s);
    """

    INSERT_ORDERS = """
        INSERT INTO orders (order_id, contract_id, model_id, salesman_id, quantity, estimated_delivery_date, lodgement_date)
        VALUES (%s, %s, %s, %s, %s, %s, %s);
    """

    INSERT_PRODUCT_MODEL = """
        INSERT INTO product_model (model_id, product_id, product_model, unit_price)
        VALUES (%s, %s, %s, %s);
    """

    INSERT_PRODUCT = """
        INSERT INTO product (product_id, product_code, product_name)
        VALUES (%s, %s, %s);
    """

    INSERT_SALESMAN = """
        INSERT INTO salesman (salesman_id, supply_center_id, salesman_number, salesman_name, gender, age, mobile_phone)
        VALUES (%s, %s, %s, %s, %s, %s, %s);
    """

    INSERT_SUPPLY_CENTER = """
        INSERT INTO supply_centers (supply_center_id, supply_center, director)
        VALUES (%s, %s, %s);
    """

    INSERT_COMPANY = """
        INSERT INTO company (company_id, supply_center_id, company_name, country, city, industry)
        VALUES (%s, %s, %s, %s, %s, %s);
    """

    CLEAN_ALL = """
        DROP TABLE IF EXISTS orders;
        DROP TABLE IF EXISTS product_model;
        DROP TABLE IF EXISTS product;
        DROP TABLE IF EXISTS contracts;
        DROP TABLE IF EXISTS salesman;
        DROP TABLE IF EXISTS company;
        DROP TABLE IF EXISTS supply_centers;
    """

    CREATE_ALL = """
        CREATE TABLE IF NOT EXISTS supply_centers (
            supply_center_id INT PRIMARY KEY,
            supply_center VARCHAR(46),
            director VARCHAR(14)
        );
        
        CREATE TABLE IF NOT EXISTS company (
            company_id INT PRIMARY KEY,
            supply_center_id INT,
            company_name VARCHAR(46),
            country VARCHAR(26),
            city VARCHAR(9),
            industry VARCHAR(37),
            FOREIGN KEY (supply_center_id) REFERENCES supply_centers(supply_center_id)
        );
        
        CREATE TABLE IF NOT EXISTS salesman (
            salesman_id INT PRIMARY KEY,
            supply_center_id INT,
            salesman_number VARCHAR(8),
            salesman_name VARCHAR(19),
            gender VARCHAR(1) CHECK (gender IN ('M', 'F', 'U')),
            age INT,
            mobile_phone VARCHAR(11),
            FOREIGN KEY (supply_center_id) REFERENCES supply_centers(supply_center_id)
        );
        
        CREATE TABLE IF NOT EXISTS contracts (
            contract_id INT PRIMARY KEY,
            company_id INT,
            contract_number VARCHAR(10),
            contract_date DATE,
            FOREIGN KEY (company_id) REFERENCES company(company_id)
        );
        
        CREATE TABLE IF NOT EXISTS product (
            product_id INT PRIMARY KEY,
            product_code VARCHAR(7),
            product_name VARCHAR(61)
        );
        
        CREATE TABLE IF NOT EXISTS product_model (
            model_id INT PRIMARY KEY,
            product_id INT,
            product_model VARCHAR(58),
            unit_price INT,
            FOREIGN KEY (product_id) REFERENCES product(product_id)
        );
        
        CREATE TABLE IF NOT EXISTS orders (
            order_id INT PRIMARY KEY,
            contract_id INT,
            model_id INT,
            salesman_id INT,
            quantity INT,
            estimated_delivery_date DATE,
            lodgement_date DATE,
            FOREIGN KEY (contract_id) REFERENCES contracts(contract_id),
            FOREIGN KEY (model_id) REFERENCES product_model(model_id),
            FOREIGN KEY (salesman_id) REFERENCES salesman(salesman_id)
        );
        
        
        ALTER TABLE supply_centers
        ADD CONSTRAINT unique_supply_center UNIQUE (supply_center);

        ALTER TABLE product
        ADD CONSTRAINT unique_product_code UNIQUE (product_code);

        ALTER TABLE company
        ADD CONSTRAINT unique_company_name UNIQUE (company_name);

        ALTER TABLE salesman
        ADD CONSTRAINT unique_salesman_number UNIQUE (salesman_number);

        ALTER TABLE product_model
        ADD CONSTRAINT unique_product_model UNIQUE (product_model);

        ALTER TABLE contracts
        ADD CONSTRAINT unique_contract_number UNIQUE (contract_number);
        
        ALTER TABLE orders
        ADD CONSTRAINT unique_order_combination UNIQUE (contract_id, model_id, salesman_id);
    """

    CREATE_ALL_WITHOUT_CONSTRAINT = """
        CREATE TABLE IF NOT EXISTS supply_centers (
            supply_center_id INT PRIMARY KEY,
            supply_center VARCHAR(46),
            director VARCHAR(14)
        );
        
        CREATE TABLE IF NOT EXISTS company (
            company_id INT PRIMARY KEY,
            supply_center_id INT,
            company_name VARCHAR(46),
            country VARCHAR(26),
            city VARCHAR(9),
            industry VARCHAR(37)
        );
        
        CREATE TABLE IF NOT EXISTS salesman (
            salesman_id INT PRIMARY KEY,
            supply_center_id INT,
            salesman_number VARCHAR(8),
            salesman_name VARCHAR(19),
            gender VARCHAR(1) CHECK (gender IN ('M', 'F', 'U')),
            age INT,
            mobile_phone VARCHAR(11)
        );
        
        CREATE TABLE IF NOT EXISTS contracts (
            contract_id INT PRIMARY KEY,
            company_id INT,
            contract_number VARCHAR(10),
            contract_date DATE
        );
        
        CREATE TABLE IF NOT EXISTS product (
            product_id INT PRIMARY KEY,
            product_code VARCHAR(7),
            product_name VARCHAR(61)
        );
        
        CREATE TABLE IF NOT EXISTS product_model (
            model_id INT PRIMARY KEY,
            product_id INT,
            product_model VARCHAR(58),
            unit_price INT
        );
        
        CREATE TABLE IF NOT EXISTS orders (
            order_id INT PRIMARY KEY,
            contract_id INT,
            model_id INT,
            salesman_id INT,
            quantity INT,
            estimated_delivery_date DATE,
            lodgement_date DATE
        );
    """

    ADD_CONSTRAINT = """
        ALTER TABLE company
        ADD CONSTRAINT fk_company_supply_center
        FOREIGN KEY (supply_center_id)
        REFERENCES supply_centers(supply_center_id);
        
        ALTER TABLE salesman
        ADD CONSTRAINT fk_salesman_supply_center
        FOREIGN KEY (supply_center_id)
        REFERENCES supply_centers(supply_center_id);
        
        ALTER TABLE contracts
        ADD CONSTRAINT fk_contracts_company
        FOREIGN KEY (company_id)
        REFERENCES company(company_id);
        
        ALTER TABLE product_model
        ADD CONSTRAINT fk_product_model_product
        FOREIGN KEY (product_id)
        REFERENCES product(product_id);
        
        ALTER TABLE orders
        ADD CONSTRAINT fk_orders_contract
        FOREIGN KEY (contract_id)
        REFERENCES contracts(contract_id);
        
        ALTER TABLE orders
        ADD CONSTRAINT fk_orders_model
        FOREIGN KEY (model_id)
        REFERENCES product_model(model_id);
        
        ALTER TABLE orders
        ADD CONSTRAINT fk_orders_salesman
        FOREIGN KEY (salesman_id)
        REFERENCES salesman(salesman_id);
        
        ALTER TABLE supply_centers
        ADD CONSTRAINT unique_supply_center UNIQUE (supply_center);

        ALTER TABLE product
        ADD CONSTRAINT unique_product_code UNIQUE (product_code);

        ALTER TABLE company
        ADD CONSTRAINT unique_company_name UNIQUE (company_name);

        ALTER TABLE salesman
        ADD CONSTRAINT unique_salesman_number UNIQUE (salesman_number);

        ALTER TABLE product_model
        ADD CONSTRAINT unique_product_model UNIQUE (product_model);

        ALTER TABLE contracts
        ADD CONSTRAINT unique_contract_number UNIQUE (contract_number);
        
        ALTER TABLE orders
        ADD CONSTRAINT unique_order_combination UNIQUE (contract_id, model_id, salesman_id);
    """

    DISABLE_TRIGGER = """
        ALTER TABLE supply_centers DISABLE TRIGGER ALL;
        ALTER TABLE company DISABLE TRIGGER ALL;
        ALTER TABLE salesman DISABLE TRIGGER ALL;
        ALTER TABLE contracts DISABLE TRIGGER ALL;
        ALTER TABLE product_model DISABLE TRIGGER ALL;
        ALTER TABLE orders DISABLE TRIGGER ALL;
        ALTER TABLE product DISABLE TRIGGER ALL;
    """

    def __init__(self):
        self.sql_sentences_map: Dict[SQLSentences, str] = {
            SQLSentences.INSERT_CONTRACTS: self.INSERT_CONTRACTS,
            SQLSentences.INSERT_ORDERS: self.INSERT_ORDERS,
            SQLSentences.INSERT_PRODUCT_MODEL: self.INSERT_PRODUCT_MODEL,
            SQLSentences.INSERT_PRODUCT: self.INSERT_PRODUCT,
            SQLSentences.INSERT_SALESMAN: self.INSERT_SALESMAN,
            SQLSentences.INSERT_SUPPLY_CENTER: self.INSERT_SUPPLY_CENTER,
            SQLSentences.INSERT_COMPANY: self.INSERT_COMPANY,

            SQLSentences.CLEAN_ALL: self.CLEAN_ALL,
            SQLSentences.CREATE_ALL: self.CREATE_ALL,

            SQLSentences.CREATE_ALL_WITHOUT_CONSTRAINT: self.CREATE_ALL_WITHOUT_CONSTRAINT,
            SQLSentences.ADD_CONSTRAINT: self.ADD_CONSTRAINT,

            SQLSentences.DISABLE_CONSTRAINT: self.DISABLE_TRIGGER
        }