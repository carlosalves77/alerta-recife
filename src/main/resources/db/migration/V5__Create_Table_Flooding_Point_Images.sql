CREATE TABLE flooding_point_images (
    id BIGSERIAL PRIMARY KEY,
    image_url TEXT NOT NULL,
    flooding_point_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_flooding_point
        FOREIGN KEY (flooding_point_id)
        REFERENCES flooding_points (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_flooding_point_images_point_id
ON flooding_point_images(flooding_point_id);