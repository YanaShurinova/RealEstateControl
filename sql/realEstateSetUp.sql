drop sequence uid_seq;
drop sequence oid_seq;
drop sequence  iid_seq;
drop sequence eid_seq;

drop table estate_status;
drop table income;
drop table outcome;
drop table real_estate;
drop table user_info;
drop table users;

CREATE TABLE estate_status (
    eid        NUMBER(3) NOT NULL,
    purchase_p NUMBER(8) NOT NULL,
    purchase_d DATE NOT NULL,
    sold       VARCHAR2(1) NOT NULL,
    sold_p     NUMBER(8),
    sold_d     DATE
);

CREATE TABLE income (
    iid       NUMBER(6) NOT NULL,
    eid       NUMBER(3) NOT NULL,
    idate    DATE NOT NULL,
    name      VARCHAR2(40) NOT NULL,
    value     NUMBER(7, 1) NOT NULL,
    icomment VARCHAR2(200)
);

ALTER TABLE income ADD CONSTRAINT incomev1_pk PRIMARY KEY ( iid );

CREATE TABLE outcome (
    oid       NUMBER(6) NOT NULL,
    eid       NUMBER(3) NOT NULL,
    odate    DATE NOT NULL,
    name      VARCHAR2(40) NOT NULL,
    value     NUMBER(7, 1) NOT NULL,
    ocomment VARCHAR2(200)
);

ALTER TABLE outcome ADD CONSTRAINT outcomev1_pk PRIMARY KEY ( oid );

CREATE TABLE real_estate (
    eid     NUMBER(6) NOT NULL,
    usid    NUMBER(3) NOT NULL,
    type    VARCHAR2(25) NOT NULL,
    address VARCHAR2(200) NOT NULL
);

ALTER TABLE real_estate ADD CONSTRAINT real_estate_pk PRIMARY KEY ( eid );

CREATE TABLE user_info (
    usid  NUMBER(3) NOT NULL,
    name   VARCHAR2(50) NOT NULL,
    regd   DATE NOT NULL,
    dscr VARCHAR2(500)
);

ALTER TABLE user_info ADD CONSTRAINT user_info_pk PRIMARY KEY ( usid    );

CREATE TABLE users (
    usid    NUMBER(3) NOT NULL,
    login    VARCHAR2(20) NOT NULL,
    password VARCHAR2(19) NOT NULL
);

ALTER TABLE users ADD CONSTRAINT users_pk PRIMARY KEY ( usid    );

ALTER TABLE real_estate
    ADD CONSTRAINT real_estate_users_fk FOREIGN KEY ( usid    )
        REFERENCES users ( usid    );

ALTER TABLE estate_status
    ADD CONSTRAINT table_5_real_estate_fk FOREIGN KEY ( eid )
        REFERENCES real_estate ( eid );

ALTER TABLE user_info
    ADD CONSTRAINT table_6_users_fk FOREIGN KEY ( usid    )
        REFERENCES users ( usid    );

commit;
create sequence eid_seq start with 1 increment by 1 NOCACHE NOCYCLE;
create sequence uid_seq start with 1 increment by 1 NOCACHE NOCYCLE;
create sequence iid_seq start with 1 increment by 1 NOCACHE NOCYCLE;
create sequence oid_seq start with 1 increment by 1 NOCACHE NOCYCLE;
commit;
