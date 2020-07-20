CREATE TABLE schedule_tbl (
    id bigint NOT NULL auto_increment,
    message VARCHAR(255) NOT NULL,
    sent BIT NOT NULL,
    time_to_send datetime(6) NOT NULL,
    to_address VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    PRIMARY KEY (id)
);