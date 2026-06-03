ALTER TABLE flooding_points ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE flooding_points
ADD CONSTRAINT fk_flooding_user FOREIGN KEY (user_id) REFERENCES users (id);