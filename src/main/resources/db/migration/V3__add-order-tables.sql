drop table if exists beer_order_line;
drop table if exists beer_order;

CREATE TABLE beer_order
(
    id                 varchar(36) NOT NULL,
    created_date       datetime(6)  DEFAULT NULL,
    customer_ref       varchar(255) DEFAULT NULL,
    last_modified_date datetime(6)  DEFAULT NULL,
    version            bigint       DEFAULT NULL,
    customer_id        varchar(36)  DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (customer_id) REFERENCES customer (id)
) ENGINE = InnoDB;

CREATE TABLE beer_order_line
(
    id                 varchar(36) NOT NULL,
    beer_id            varchar(36) DEFAULT NULL,
    created_date       datetime(6) DEFAULT NULL,
    last_modified_date datetime(6) DEFAULT NULL,
    order_quantity     int         DEFAULT NULL,
    quantity_allocated int         DEFAULT NULL,
    version            bigint      DEFAULT NULL,
    beer_order_id      varchar(36) DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (beer_order_id) REFERENCES beer_order (id),
    CONSTRAINT FOREIGN KEY (beer_id) REFERENCES beer (id)
) ENGINE = InnoDB;
drop table if exists beer_order_shipment;


--this is only for demonstration purpose, lets say I cant modify already existing beer order table(OneToOne relationship)
CREATE TABLE beer_order_shipment
(
    id                 VARCHAR(36) NOT NULL PRIMARY KEY,
    beer_order_id      VARCHAR(36) UNIQUE,
    tracking_number    VARCHAR(50),
    created_date       TIMESTAMP,
    last_modified_date DATETIME(6) DEFAULT NULL,
    version            BIGINT      DEFAULT NULL,
    CONSTRAINT bos_pk FOREIGN KEY (beer_order_id) REFERENCES beer_order (id)
) ENGINE = InnoDB;

ALTER TABLE beer_order
    ADD COLUMN beer_order_shipment_id VARCHAR(36);

ALTER TABLE beer_order
    ADD CONSTRAINT bos_shipment_fk
        FOREIGN KEY (beer_order_shipment_id) REFERENCES beer_order_shipment (id);