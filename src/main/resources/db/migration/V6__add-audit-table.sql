CREATE TABLE beer_audit (
      audit_id VARCHAR(36) NOT NULL,
      id VARCHAR(36) NOT NULL,
      version INTEGER,
      beer_name VARCHAR(50) NOT NULL,
      beer_style INTEGER NOT NULL,
      upc VARCHAR(255) NOT NULL,
      quantity_on_hand INTEGER,
      price DECIMAL(38,2) NOT NULL,
      created_date datetime(6),
      update_date datetime(6),
      created_date_audit datetime(6),
      principal_name VARCHAR(255),
      audit_event_type VARCHAR(255),
      PRIMARY KEY (audit_id)
) engine=InnoDB;