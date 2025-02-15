CREATE TABLE customer_details (
    customer_id         INT PRIMARY KEY,
    customer_name       VARCHAR(255) NOT NULL,
    email              VARCHAR(255) UNIQUE,
    phone_number       VARCHAR(20),
    address            VARCHAR(500),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_information (
    product_id         INT PRIMARY KEY,
    product_name       VARCHAR(255) NOT NULL,
    category          VARCHAR(100),
    price             DECIMAL(10,2),
    stock_quantity    INT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sales_data (
    sale_id           INT PRIMARY KEY,
    customer_id       INT,
    product_id        INT,
    quantity_sold     INT,
    sale_date         DATE,
    total_price       DECIMAL(10,2),
    FOREIGN KEY (customer_id) REFERENCES customer_details(customer_id),
    FOREIGN KEY (product_id) REFERENCES product_information(product_id)
);

CREATE TABLE supplier_details (
    supplier_id       INT PRIMARY KEY,
    supplier_name     VARCHAR(255) NOT NULL,
    contact_person    VARCHAR(255),
    phone_number      VARCHAR(20),
    email             VARCHAR(255),
    address           VARCHAR(500),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory_stock (
    stock_id          INT PRIMARY KEY,
    product_id        INT,
    supplier_id       INT,
    quantity_available INT,
    last_updated      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product_information(product_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier_details(supplier_id)
);

CREATE TABLE orders (
    order_id          INT PRIMARY KEY,
    customer_id       INT,
    order_date        DATE,
    total_amount      DECIMAL(10,2),
    status            VARCHAR(50),
    FOREIGN KEY (customer_id) REFERENCES customer_details(customer_id)
);

CREATE TABLE order_items (
    order_item_id     INT PRIMARY KEY,
    order_id         INT,
    product_id       INT,
    quantity         INT,
    price_per_unit   DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES product_information(product_id)
);

CREATE TABLE payments (
    payment_id       INT PRIMARY KEY,
    order_id        INT,
    payment_date    DATE,
    amount_paid     DECIMAL(10,2),
    payment_method  VARCHAR(50),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

CREATE TABLE employee_details (
    employee_id      INT PRIMARY KEY,
    full_name       VARCHAR(255),
    email           VARCHAR(255) UNIQUE,
    phone_number    VARCHAR(20),
    department      VARCHAR(100),
    salary          DECIMAL(10,2),
    hire_date       DATE
);

CREATE TABLE shipping_details (
    shipping_id      INT PRIMARY KEY,
    order_id        INT,
    shipping_date   DATE,
    delivery_date   DATE,
    tracking_number VARCHAR(50),
    courier_company VARCHAR(100),
    status          VARCHAR(50),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
