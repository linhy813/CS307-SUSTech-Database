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


    ;






    private final String sql;


    MyUsedSQLs(String sentence) {
        this.sql = sentence;
    }

    public String getSql() {
        return sql;
    }
}
