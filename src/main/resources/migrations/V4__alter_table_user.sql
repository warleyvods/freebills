ALTER TABLE categories ADD COLUMN user_id BIGSERIAL;

ALTER TABLE categories ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE categories ADD COLUMN category_type VARCHAR(50);