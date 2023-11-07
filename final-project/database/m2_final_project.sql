-- database m2_final_project
BEGIN TRANSACTION;

-- *************************************************************************************************
-- Drop all db objects in the proper order
-- *************************************************************************************************
DROP TABLE IF EXISTS wishlist_item;
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS users;

-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************

--users (name is pluralized because 'user' is a SQL keyword)
CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	name varchar(50) NOT NULL,
	address varchar(100) NULL,
	city varchar(50) NULL,
	state_code char(2) NULL,
	zip varchar(5) NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

-- product
CREATE TABLE product (
	product_id SERIAL,
	product_sku varchar(20) NOT NULL UNIQUE,
	name varchar(50) NOT NULL,
	description varchar,
	price decimal(8,2) NOT NULL,
	image_name varchar(200),
	CONSTRAINT PK_product PRIMARY KEY (product_id)
);

-- wishlist
CREATE TABLE wishlist (
	wishlist_id SERIAL,
	user_id int NOT NULL,
	name varchar(50) NOT NULL,
	CONSTRAINT PK_wishlist PRIMARY KEY (wishlist_id),
	CONSTRAINT FK_wishlist_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- wishlist item
CREATE TABLE wishlist_item (
	wishlist_item_id SERIAL,
	wishlist_id int NOT NULL,
	product_id int NOT NULL,
	CONSTRAINT PK_wishlist_item PRIMARY KEY (wishlist_item_id),
	CONSTRAINT FK_wishlist_item_wishlist FOREIGN KEY (wishlist_id) REFERENCES wishlist(wishlist_id),
	CONSTRAINT FK_wishlist_item_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);
CREATE UNIQUE INDEX IX_wishlist_item_wishlist_product ON wishlist_item(wishlist_id, product_id);

-- cart item
CREATE TABLE cart_item (
	cart_item_id SERIAL,
	user_id int NOT NULL,
	product_id int NOT NULL,
	quantity int NOT NULL DEFAULT(1),
	CONSTRAINT PK_cart_item PRIMARY KEY (cart_item_id),
	CONSTRAINT FK_user FOREIGN KEY (user_id) REFERENCES users(user_id),
	CONSTRAINT FK_cart_item_product FOREIGN KEY (product_id) REFERENCES product(product_id)
);
CREATE UNIQUE INDEX IX_cart_item_user_product ON cart_item(user_id, product_id);

-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************

-- Users
-- Password for all users is password
INSERT INTO users (username,password_hash,role, name, address, city, state_code, zip) VALUES 
    ('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_USER',  'Jack O''Lantern', null, 'Cleveland', 'OH', '44123'),
    ('admin','$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem','ROLE_ADMIN', 'Jill O''Lantern', null, 'Beverly Hills', 'CA', '90210');

-- Products
INSERT INTO product (product_sku, name, description, price, image_name) VALUES 
    ('MUG-023', 'Solar Geeks coffee mug',  'Start your day off right!',  14.99, 'Product-MUG-023.jpg'),
    ('YET-001', 'Solar Geeks Yeti',        'Keep cool all day long.',    21.99, 'Product-YET-001.jpg'),
    ('ART-256', 'Galactic poster',         'Beautiful view of a galaxy',  9.59, 'Product-ART-256.jpg'),
    ('TOY-978', 'Toy rocket',              'To infinite imagination',    39.99, 'Product-TOY-978.jpg'),
    ('EAT-235', 'Astronaut ice cream',     'As cold as space',            5.79, 'Product-EAT-215.jpg'),
    ('HAT-928', 'Solar Geeks baseball cap','Look stylish with our logo', 16.89, 'Product-HAT-908.jpg'),
    ('LIT-612', 'Intro to Astrophysics',   'Learn about astrophysics',    7.99, 'Product-LIT-612.jpg');

-- Carts
INSERT INTO cart_item(user_id, product_id, quantity) VALUES
    (1, 2, 3),
    (1, 4, 1),
    (1, 1, 2);

-- Wishlists
INSERT INTO wishlist (user_id, name) VALUES 
	(1, 'My First Wishlist'),
	(1, 'My Second Wishlist');
	
-- Wishlist items
INSERT INTO wishlist_item (wishlist_id, product_id) VALUES
	(1, 4),
	(1, 5),
	(1, 6);

COMMIT TRANSACTION;
