BEGIN TRANSACTION;

-- Users
INSERT INTO users (username,password_hash,role, name, address, city, state_code, zip) VALUES ('user1','user1','ROLE_USER', 'User1 Name', 'User1 address', 'Cleveland', 'OH', '44123');
INSERT INTO users (username,password_hash,role, name, address, city, state_code, zip) VALUES ('user2','user2','ROLE_USER', 'User2 Name', null, 'Beverly Hills', 'CA', '90210');
INSERT INTO users (username,password_hash,role, name, address, city, state_code, zip) VALUES ('user3','user3','ROLE_USER', 'User3 Name', 'User3 address', 'Chicago', 'IL', '60609');

-- Products
INSERT INTO product (product_sku, name, description, price, image_name) VALUES
    ('SKU-001', 'Product 1', 'Product description 1.', 14.99, 'Product001.jpg'),
    ('SKU-002', 'Product 2', 'Product description 2.', 21.99, 'Product002.jpg'),
    ('SKU-003', 'Product name 3', 'Product description 3.',  3.59, 'Product003.jpg'),
    ('SKU-004', 'Product name 4', 'Product description 4.',  4.69, 'Product004.jpg'),
    ('SKU-005', 'Product name 5', 'Product description 5.',  5.79, 'Product005.jpg'),
    ('SKU-006', 'Product name 6', 'Product description 6.',  6.89, 'Product006.jpg'),
    ('SKU-007', 'Product name 7', 'Product description 7.',  7.99, 'Product007.jpg');
COMMIT TRANSACTION;
