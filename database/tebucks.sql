BEGIN TRANSACTION;

DROP TABLE IF EXISTS users, transfer, account, logger_exceptions;

CREATE TABLE users (
	user_id serial NOT NULL,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(50),
	role varchar(20),
	CONSTRAINT pk_users PRIMARY KEY (user_id),
	CONSTRAINT uq_username UNIQUE (username)
);

CREATE TABLE transfer (
	transfer_id serial NOT NULL,
	transfer_type varchar(10) NOT NULL,
	from_user_id int NOT NULL,
	to_user_id int NOT NULL,
	amount decimal NOT NULL,
	transfer_status varchar(25) NOT NULL,
	CONSTRAINT pk_transfer_id PRIMARY KEY (transfer_id),
	CONSTRAINT ck_transfer_type CHECK (transfer_type IN ('Send','Request')),
	CONSTRAINT ck_transfer_status CHECK (transfer_status IN ('Pending','Approved','Rejected')),
	CONSTRAINT ck_not_same_user CHECK (from_user_id <> to_user_id),

	CONSTRAINT fk_from_user_id FOREIGN KEY (from_user_id) references users(user_id),
	CONSTRAINT fk_to_user_id FOREIGN KEY (to_user_id) references users(user_id)
);


CREATE TABLE account (
	user_id int NOT NULL,
	balance decimal NOT NULL,
	CONSTRAINT pk_user_id PRIMARY KEY (user_id),
	CONSTRAINT fk_user_id FOREIGN KEY (user_id) references users(user_id)

);

CREATE TABLE logger_exceptions (
	exception_id serial not null,
	exception_date varchar(50) not null,
	description varchar(100) not null,
	from_user varchar(50) not null,
	to_user varchar(50) not null,
	amount decimal not null,

	constraint pk_exception_id primary key (exception_id)

);


COMMIT TRANSACTION;