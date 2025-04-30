-- clean all
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS product_model;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS contracts;
DROP TABLE IF EXISTS salesman;
DROP TABLE IF EXISTS company;
DROP TABLE IF EXISTS supply_centers;


-- create all
CREATE TABLE IF NOT EXISTS supply_centers
(
    supply_center_id INT PRIMARY KEY,
    supply_center    VARCHAR(46),
    director         VARCHAR(14)
);

CREATE TABLE IF NOT EXISTS company
(
    company_id       INT PRIMARY KEY,
    supply_center_id INT,
    company_name     VARCHAR(46),
    country          VARCHAR(26),
    city             VARCHAR(9),
    industry         VARCHAR(37),
    FOREIGN KEY (supply_center_id) REFERENCES supply_centers (supply_center_id)
);

CREATE TABLE IF NOT EXISTS salesman
(
    salesman_id      INT PRIMARY KEY,
    supply_center_id INT,
    salesman_number  VARCHAR(8),
    salesman_name    VARCHAR(19),
    gender           VARCHAR(1) CHECK (gender IN ('M', 'F', 'U')),
    age              INT,
    mobile_phone     VARCHAR(11),
    FOREIGN KEY (supply_center_id) REFERENCES supply_centers (supply_center_id)
);

CREATE TABLE IF NOT EXISTS contracts
(
    contract_id     INT PRIMARY KEY,
    company_id      INT,
    contract_number VARCHAR(10),
    contract_date   DATE,
    FOREIGN KEY (company_id) REFERENCES company (company_id)
);

CREATE TABLE IF NOT EXISTS product
(
    product_id   INT PRIMARY KEY,
    product_code VARCHAR(7),
    product_name VARCHAR(61)
);

CREATE TABLE IF NOT EXISTS product_model
(
    model_id      INT PRIMARY KEY,
    product_id    INT,
    product_model VARCHAR(58),
    unit_price    INT,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id                INT PRIMARY KEY,
    contract_id             INT,
    model_id                INT,
    salesman_id             INT,
    quantity                INT,
    estimated_delivery_date DATE,
    lodgement_date          DATE,
    FOREIGN KEY (contract_id) REFERENCES contracts (contract_id),
    FOREIGN KEY (model_id) REFERENCES product_model (model_id),
    FOREIGN KEY (salesman_id) REFERENCES salesman (salesman_id)
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

--     CREATE_ALL_WITHOUT_CONSTRAINT
CREATE TABLE IF NOT EXISTS supply_centers
(
    supply_center_id INT PRIMARY KEY,
    supply_center    VARCHAR(46),
    director         VARCHAR(14)
);

CREATE TABLE IF NOT EXISTS company
(
    company_id       INT PRIMARY KEY,
    supply_center_id INT,
    company_name     VARCHAR(46),
    country          VARCHAR(26),
    city             VARCHAR(9),
    industry         VARCHAR(37)
);

CREATE TABLE IF NOT EXISTS salesman
(
    salesman_id      INT PRIMARY KEY,
    supply_center_id INT,
    salesman_number  VARCHAR(8),
    salesman_name    VARCHAR(19),
    gender           VARCHAR(1) CHECK (gender IN ('M', 'F', 'U')),
    age              INT,
    mobile_phone     VARCHAR(11)
);

CREATE TABLE IF NOT EXISTS contracts
(
    contract_id     INT PRIMARY KEY,
    company_id      INT,
    contract_number VARCHAR(10),
    contract_date   DATE
);

CREATE TABLE IF NOT EXISTS product
(
    product_id   INT PRIMARY KEY,
    product_code VARCHAR(7),
    product_name VARCHAR(61)
);

CREATE TABLE IF NOT EXISTS product_model
(
    model_id      INT PRIMARY KEY,
    product_id    INT,
    product_model VARCHAR(58),
    unit_price    INT
);

CREATE TABLE IF NOT EXISTS orders
(
    order_id                INT PRIMARY KEY,
    contract_id             INT,
    model_id                INT,
    salesman_id             INT,
    quantity                INT,
    estimated_delivery_date DATE,
    lodgement_date          DATE
);


--     ADD_CONSTRAINT
ALTER TABLE company
    ADD CONSTRAINT fk_company_supply_center
        FOREIGN KEY (supply_center_id)
            REFERENCES supply_centers (supply_center_id);

ALTER TABLE salesman
    ADD CONSTRAINT fk_salesman_supply_center
        FOREIGN KEY (supply_center_id)
            REFERENCES supply_centers (supply_center_id);

ALTER TABLE contracts
    ADD CONSTRAINT fk_contracts_company
        FOREIGN KEY (company_id)
            REFERENCES company (company_id);

ALTER TABLE product_model
    ADD CONSTRAINT fk_product_model_product
        FOREIGN KEY (product_id)
            REFERENCES product (product_id);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_contract
        FOREIGN KEY (contract_id)
            REFERENCES contracts (contract_id);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_model
        FOREIGN KEY (model_id)
            REFERENCES product_model (model_id);

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_salesman
        FOREIGN KEY (salesman_id)
            REFERENCES salesman (salesman_id);

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


--     DISABLE_TRIGGER
ALTER TABLE supply_centers
    DISABLE TRIGGER ALL;
ALTER TABLE company
    DISABLE TRIGGER ALL;
ALTER TABLE salesman
    DISABLE TRIGGER ALL;
ALTER TABLE contracts
    DISABLE TRIGGER ALL;
ALTER TABLE product_model
    DISABLE TRIGGER ALL;
ALTER TABLE orders
    DISABLE TRIGGER ALL;
ALTER TABLE product
    DISABLE TRIGGER ALL;


-- 1. 不同性别的销售员分别有多少个
select distinct gender, count(distinct salesman_name)
from salesman
group by gender;

-- 2. 每一个supply center中有多少公司
select supply_center, count(distinct company_name)
from company
         join supply_centers on company.supply_center_id = supply_centers.supply_center_id
group by supply_center
order by supply_center ASC;

-- 3. 每一个supply center中有多少销售员
select s.supply_center, count(distinct salesman_id)
from supply_centers s
         join salesman m on s.supply_center_id = m.supply_center_id
group by s.supply_center
order by supply_center ASC;

-- 4. 年龄在某个上界或下界区间的销售员有多少个
select count(distinct salesman)
from salesman
where age > 42
  and age < 45;

-- 5. 每一个supply center中有多少国家
select s.supply_center, count(distinct country)
from company c
         join supply_centers s on c.supply_center_id = s.supply_center_id
group by s.supply_center
order by supply_center ASC;

-- 6. 根据某⼀个国家名称，列出其所有的公司名称，及其公司所属⾏业
-- 添加国家名称限制条件
select distinct country, company_name, industry
from company
where country = 'Italy'
order by country, company_name;

select distinct country, company_name, industry
from company
where country = 'Canada'
order by country, company_name;

-- 7. 根据某⼀个产品编码，列出其所有的产品型号、及对应的单价
-- 添加产品编码限制条件
-- 为直接比较，output里的text转成integer
select distinct p.product_code, m.product_model, m.unit_price
from product p
         join product_model m on p.product_id = m.product_id
where product_code = 'B56K283'
order by p.product_code, m.product_model, m.unit_price;

select distinct p.product_code, m.product_model, m.unit_price
from product p
         join product_model m on p.product_id = m.product_id
where product_code = 'C78629B'
order by p.product_code, m.product_model, m.unit_price;

-- 8. 根据某⼀个合同编码，列出合同中所有订单内容，每个订单包含（产品型号、购买数量、销售员名称、实际到达⽇期）
select distinct c.contract_number, p.product_code, m.product_model, o.quantity, s.salesman_name, o.lodgement_date
from contracts c
         join orders o on c.contract_id = o.contract_id
         join salesman s on o.salesman_id = s.salesman_id
         join product_model m on o.model_id = m.model_id
         join product p on m.product_id = p.product_id
where contract_number = 'CSE0000001'
order by c.contract_number, m.product_model, o.lodgement_date, s.salesman_name, o.lodgement_date;

select distinct c.contract_number, p.product_code, m.product_model, o.quantity, s.salesman_name, o.lodgement_date
from contracts c
         join orders o on c.contract_id = o.contract_id
         join salesman s on o.salesman_id = s.salesman_id
         join product_model m on o.model_id = m.model_id
         join product p on m.product_id = p.product_id
where contract_number = 'CSE0000267'
order by c.contract_number, m.product_model, o.lodgement_date, s.salesman_name, o.lodgement_date;

select distinct c.contract_number, m.product_model, o.quantity, s.salesman_name, o.lodgement_date
from contracts c
         join orders o on c.contract_id = o.contract_id
         join salesman s on o.salesman_id = s.salesman_id
         join product_model m on o.model_id = m.model_id
where contract_number = 'CSE0000064'
order by c.contract_number, m.product_model, o.lodgement_date, s.salesman_name, o.lodgement_date;
