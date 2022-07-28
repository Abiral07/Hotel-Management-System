CREATE TABLE hms.address(
	aid bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	country varchar(30),
    state varchar(30),
    city varchar(30),
    ward int
);
CREATE TABLE hms.role(
	role_id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	role_name varchar(20) UNIQUE NOT NULL,
    description varchar(100)
);

CREATE TABLE hms.users(
	uid bigint NOT NULL AUTO_INCREMENT,
	user_name varchar(50) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    full_name varchar(100) NOT NULL,
    email varchar(255) UNIQUE NOT NULL,
    mobile varchar(255) UNIQUE NOT NULL,
    gender enum("MALE","FEMALE","OTHERS"),
    dob date,
    age int,
    isactive boolean,
    address bigint,
    PRIMARY KEY(uid),
    CONSTRAINT fk_addid FOREIGN KEY(address) REFERENCES address(aid)
);
CREATE TABLE hms.rooms(
	rid bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	type enum("DIAMOND","PLATINUM","GOLD","SILVER","BRONZE"),
    price decimal,
    status bit(1)
);
CREATE TABLE hms.reservation(
	reservation_id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uid bigint not null,
    rid bigint not null,
    in_date date NOT NULL DEFAULT (CURRENT_DATE) ,
    out_date date,
    type enum("DIAMOND","PLATINUM","GOLD","SILVER","BRONZE"),
    total_price decimal,
    payment_gateway varchar(50),
    payment_status bit(1),
    reservation_status enum("PENDING","SUCCESS","CHECKED_IN","CHECKED_OUT","FAILED"),
    created_at datetime(6),
    updated_at datetime(6),
	CONSTRAINT fk_cid FOREIGN KEY(uid) REFERENCES users(uid),
	CONSTRAINT fk_rid FOREIGN KEY(rid) REFERENCES rooms(rid)
);
create table users_role (
uid bigint not null, 
role_id bigint not null, 
constraint FK7jol9jrbtlt6ctiehegh6besp foreign key (role_id) references role (role_id),
constraint FKjo75lc8w0jikps7b3tshmdk7f foreign key (eid) references employee (eid),
primary key (eid, role_id)) engine=InnoDB;
create table hibernate_sequence (next_val bigint) engine=InnoDB;
alter table employee_role drop index UK_7ffk4rmk25rah5piv18v67007;
alter table employee_role add constraint UK_7ffk4rmk25rah5piv18v67007 unique (role_id);
alter table employee_role ;
alter table employee_role ;