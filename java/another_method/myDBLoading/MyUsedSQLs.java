package myz.myDBLoading;

public enum MyUsedSQLs {
//    SQL1("select * from color_names where name = 'Aqua'"),
INSERT_SUPPLY_CENTERS("insert into public.supply_centers (supply_center_id,supply_center, director) VALUES (?,?,?);"),

    INSERT_PRODUCT("insert into public.product (product_id, product_code, product_name) VALUES (?,?,?);"),

    INSERT_SALESMAN("insert into public.salesman (salesman_id, supply_center_id, salesman_number, salesman_name, gender, age, mobile_phone)" +
            " VALUES (?,?,?,?,?,?,?);"),

    INSERT_COMPANY("insert into public.company (company_id, supply_center_id, company_name, country, city, industry)" +
            " VALUES (?,?,?,?,?,?);"),

    INSERT_PRODUCT_MODEL("insert into public.product_model " +
            "(model_id,product_id, product_model, unit_price) VALUES (?,?,?,?);"),

    INSERT_CONTRACTS("insert into public.contracts " +
            "(contract_id,contract_number, company_id, contract_date) VALUES (?,?,?,?);"),

    INSERT_ORDERS("insert into public.orders " +
            "(order_id, contract_id, model_id, salesman_id, quantity, estimated_delivery_date, lodgement_date)" +
            " VALUES (?,?,?,?,?,?,?);"),


    CLEAN_ALL_DATA("DROP TABLE IF EXISTS orders;\n" +
            "DROP TABLE IF EXISTS product_model;\n" +
            "DROP TABLE IF EXISTS product;\n" +
            "DROP TABLE IF EXISTS contracts;\n" +
            "DROP TABLE IF EXISTS salesman;\n" +
            "DROP TABLE IF EXISTS company;\n" +
            "DROP TABLE IF EXISTS supply_centers;\n" +
            "\n" +
            "\n" +
            "-- create all\n" +
            "CREATE TABLE IF NOT EXISTS supply_centers\n" +
            "(\n" +
            "    supply_center_id INT PRIMARY KEY,\n" +
            "    supply_center    VARCHAR(44),\n" +
            "    director         VARCHAR(14)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS company\n" +
            "(\n" +
            "    company_id       INT PRIMARY KEY,\n" +
            "    supply_center_id INT,\n" +
            "    company_name     VARCHAR(46),\n" +
            "    country          VARCHAR(26),\n" +
            "    city             VARCHAR(9),\n" +
            "    industry         VARCHAR(37),\n" +
            "    FOREIGN KEY (supply_center_id) REFERENCES supply_centers (supply_center_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS salesman\n" +
            "(\n" +
            "    salesman_id      INT PRIMARY KEY,\n" +
            "    supply_center_id INT,\n" +
            "    salesman_number  VARCHAR(8),\n" +
            "    salesman_name    VARCHAR(19),\n" +
            "    gender           VARCHAR(1) CHECK (gender IN ('M', 'F', 'U')),\n" +
            "    age              INT,\n" +
            "    mobile_phone     VARCHAR(11),\n" +
            "    FOREIGN KEY (supply_center_id) REFERENCES supply_centers (supply_center_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS contracts\n" +
            "(\n" +
            "    contract_id     INT PRIMARY KEY,\n" +
            "    company_id      INT,\n" +
            "    contract_number VARCHAR(10),\n" +
            "    contract_date   DATE,\n" +
            "    FOREIGN KEY (company_id) REFERENCES company (company_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS product\n" +
            "(\n" +
            "    product_id   INT PRIMARY KEY,\n" +
            "    product_code VARCHAR(7),\n" +
            "    product_name VARCHAR(61)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS product_model\n" +
            "(\n" +
            "    model_id      INT PRIMARY KEY,\n" +
            "    product_id    INT,\n" +
            "    product_model VARCHAR(58),\n" +
            "    unit_price    INT,\n" +
            "    FOREIGN KEY (product_id) REFERENCES product (product_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE IF NOT EXISTS orders\n" +
            "(\n" +
            "    order_id                INT PRIMARY KEY,\n" +
            "    contract_id             INT,\n" +
            "    model_id                INT,\n" +
            "    salesman_id             INT,\n" +
            "    quantity                INT,\n" +
            "    estimated_delivery_date DATE,\n" +
            "    lodgement_date          DATE,\n" +
            "    FOREIGN KEY (contract_id) REFERENCES contracts (contract_id),\n" +
            "    FOREIGN KEY (model_id) REFERENCES product_model (model_id),\n" +
            "    FOREIGN KEY (salesman_id) REFERENCES salesman (salesman_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "ALTER TABLE supply_centers\n" +
            "    ADD CONSTRAINT unique_supply_center UNIQUE (supply_center);\n" +
            "\n" +
            "ALTER TABLE product\n" +
            "    ADD CONSTRAINT unique_product_code UNIQUE (product_code);\n" +
            "\n" +
            "ALTER TABLE company\n" +
            "    ADD CONSTRAINT unique_company_name UNIQUE (company_name);\n" +
            "\n" +
            "ALTER TABLE salesman\n" +
            "    ADD CONSTRAINT unique_salesman_number UNIQUE (salesman_number);\n" +
            "\n" +
            "ALTER TABLE product_model\n" +
            "    ADD CONSTRAINT unique_product_model UNIQUE (product_model);\n" +
            "\n" +
            "ALTER TABLE contracts\n" +
            "    ADD CONSTRAINT unique_contract_number UNIQUE (contract_number);\n" +
            "\n" +
            "ALTER TABLE orders\n" +
            "    ADD CONSTRAINT unique_order_combination UNIQUE (contract_id, model_id, salesman_id);"

    ),

    NO_CONSTRAINT_TABLE("drop table if exists Orders;\n" +
            "drop table if exists Contracts;\n" +
            "drop table if exists Product_Model;\n" +
            "drop table if exists Company;\n" +
            "drop table if exists Product;\n" +
            "drop table if exists Salesman;\n" +
            "drop table if exists Supply_Centers;\n" +
            "\n" +
            "drop table if exists Orders_Raw;\n" +
            "drop table if exists Contracts_Raw;\n" +
            "drop table if exists Product_Model_Raw;\n" +
            "drop table if exists Company_Raw;\n" +
            "drop table if exists Product_Raw;\n" +
            "drop table if exists Salesman_Raw;\n" +
            "drop table if exists Supply_Centers_Raw;\n"+
            "\n" +
            "create table if not exists Supply_Centers_Raw(\n" +
            "    supply_center_id int,\n" +
            "    supply_center varchar(66),\n" +
            "    director varchar(14)\n" +
            ");\n" +
            "create table if not exists Product_Raw(\n" +
            "    product_id int,\n" +
            "    product_code varchar(7),\n" +
            "    product_name varchar(61)\n" +
            ");\n" +
            "create table if not exists Salesman_Raw(\n" +
            "    salesman_id int,\n" +
            "    supply_center_id int,\n" +
            "    salesman_number VARCHAR(8),\n" +
            "    salesman_name varchar(19),\n" +
            "    gender varchar(1) check ( gender in ('F','M','U') ),\n" +
            "    age int,\n" +
            "    mobile_phone varchar(11)\n" +
            ");\n" +
            "create table if not exists Company_Raw(\n" +
            "    company_id int,\n" +
            "    supply_center_id int,\n" +
            "    company_name varchar(46),\n" +
            "    country varchar(26),\n" +
            "    city varchar(9),\n" +
            "    industry varchar(37)\n" +
            ");\n" +
            "create table if not exists Product_Model_Raw(\n" +
            "    model_id int,\n" +
            "    product_id int,\n" +
            "    product_model varchar(58),\n" +
            "    unit_price int\n" +
            ");\n" +
            "create table if not exists Contracts_Raw(\n" +
            "    contract_id int,\n" +
            "    contract_number varchar(10),\n" +
            "    company_id int,\n" +
            "    contract_date date\n" +
            ");\n" +
            "create table if not exists Orders_Raw(\n" +
            "    order_id int,\n" +
            "    contract_id int,\n" +
            "    model_id int,\n" +
            "    salesman_id int,\n" +
            "    quantity int,\n" +
            "    estimated_delivery_date date,\n" +
            "    lodgement_date date\n" +
            ");"),

    INSERT_SUPPLY_CENTERS_RAW("insert into public.supply_centers_raw (supply_center_id,supply_center, director) VALUES (?,?,?);"),

    INSERT_PRODUCT_RAW("insert into public.product_raw (product_id, product_code, product_name) VALUES (?,?,?);"),

    INSERT_SALESMAN_RAW("insert into public.salesman_raw (salesman_id, supply_center_id, salesman_number, salesman_name, gender, age, mobile_phone)" +
                            " VALUES (?,?,?,?,?,?,?);"),

    INSERT_COMPANY_RAW("insert into public.company_raw (company_id, supply_center_id, company_name, country, city, industry)" +
                           " VALUES (?,?,?,?,?,?);"),

    INSERT_PRODUCT_MODEL_RAW("insert into public.product_model_raw " +
                                 "(model_id,product_id, product_model, unit_price) VALUES (?,?,?,?);"),

    INSERT_CONTRACTS_RAW("insert into public.contracts_raw " +
                             "(contract_id,contract_number, company_id, contract_date) VALUES (?,?,?,?);"),

    INSERT_ORDERS_RAW("insert into public.orders_raw " +
                          "(order_id, contract_id, model_id, salesman_id, quantity, estimated_delivery_date, lodgement_date)" +
                          " VALUES (?,?,?,?,?,?,?);"),


    GENERATE_ALTERED_TABLE("create table if not exists Supply_Centers(\n" +
            "    supply_center_id serial primary key,\n" +
            "    supply_center varchar(66) unique,\n" +
            "    director varchar(14)\n" +
            ");\n" +
            "INSERT INTO Supply_Centers (supply_center_id, supply_center, director)\n" +
            "SELECT DISTINCT supply_center_id, supply_center, director\n" +
            "FROM Supply_Centers_Raw order by supply_center_id;\n" +
            "\n" +
            "create table if not exists Product(\n" +
            "    product_id serial primary key,\n" +
            "    product_code varchar(7) unique,\n" +
            "    product_name varchar(61)\n" +
            ");\n" +
            "INSERT INTO Product (product_id, product_code, product_name)\n" +
            "SELECT DISTINCT product_id, product_code, product_name\n" +
            "FROM Product_Raw order by product_id;\n" +
            "\n" +
            "create table if not exists Salesman(\n" +
            "    salesman_id int primary key,\n" +
            "    supply_center_id int references Supply_Centers(supply_center_id),\n" +
            "    salesman_number VARCHAR(8) unique,\n" +
            "    salesman_name varchar(19),\n" +
            "    gender varchar(1) check ( gender in ('F','M','U') ),\n" +
            "    age int,\n" +
            "    mobile_phone varchar(11)\n" +
            ");\n" +
            "INSERT INTO Salesman (salesman_id,supply_center_id,salesman_number,salesman_name,gender,age,mobile_phone)\n" +
            "SELECT DISTINCT salesman_id,supply_center_id,salesman_number,salesman_name,gender,age,mobile_phone\n" +
            "FROM Salesman_Raw order by salesman_id;\n" +
            "\n" +
            "create table if not exists Company(\n" +
            "    company_id serial primary key,\n" +
            "    supply_center_id int references Supply_Centers(supply_center_id),\n" +
            "    company_name varchar(46) unique,\n" +
            "    country varchar(26),\n" +
            "    city varchar(9),\n" +
            "    industry varchar(37)\n" +
            ");\n" +
            "INSERT INTO Company (company_id,supply_center_id,company_name,country,city,industry)\n" +
            "SELECT DISTINCT company_id,supply_center_id,company_name,country,city,industry\n" +
            "FROM Company_Raw order by company_id;\n" +
            "\n" +
            "create table if not exists Product_Model(\n" +
            "    model_id serial primary key,\n" +
            "    product_id int references Product(product_id),\n" +
            "    product_model varchar(58) unique,\n" +
            "    unit_price int\n" +
            ");\n" +
            "INSERT INTO Product_Model (model_id,product_id,product_model,unit_price)\n" +
            "SELECT DISTINCT model_id,product_id,product_model,unit_price\n" +
            "FROM Product_Model_Raw order by model_id;\n" +
            "\n" +
            "create table if not exists Contracts(\n" +
            "    contract_id serial primary key,\n" +
            "    contract_number varchar(10) unique,\n" +
            "    company_id int references Company(company_id),\n" +
            "    contract_date date\n" +
            ");\n" +
            "INSERT INTO Contracts (contract_id,contract_number,company_id,contract_date)\n" +
            "SELECT DISTINCT contract_id,contract_number,company_id,contract_date\n" +
            "FROM Contracts_Raw order by contract_id;\n" +
            "\n" +
            "create table if not exists Orders(\n" +
            "    order_id serial primary key,\n" +
            "    contract_id int references Contracts(contract_id),\n" +
            "    model_id int references Product_Model(model_id),\n" +
            "    salesman_id int references Salesman(salesman_id),\n" +
            "    quantity int,\n" +
            "    estimated_delivery_date date,\n" +
            "    lodgement_date date,\n" +
            "    UNIQUE (contract_id,model_id,salesman_id)\n" +
            ");\n" +
            "INSERT INTO Orders (order_id,contract_id,model_id,salesman_id,quantity,estimated_delivery_date,lodgement_date)\n" +
            "SELECT DISTINCT order_id,contract_id,model_id,salesman_id,quantity,estimated_delivery_date,lodgement_date\n" +
            "FROM Orders_Raw order by order_id;"+
            "\n" +
            "\n" +
            "drop table if exists Orders_Raw;\n" +
            "drop table if exists Contracts_Raw;\n" +
            "drop table if exists Product_Model_Raw;\n" +
            "drop table if exists Company_Raw;\n" +
            "drop table if exists Product_Raw;\n" +
            "drop table if exists Salesman_Raw;\n" +
            "drop table if exists Supply_Centers_Raw;")
    ;






    private final String sql;


    MyUsedSQLs(String sentence) {
        this.sql = sentence;
    }

    public String getSql() {
        return sql;
    }
}
