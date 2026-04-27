CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    password VARCHAR(255),
    photo TEXT,
    email_verified BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE recipes (
    id VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    image TEXT,
    name VARCHAR(255),
    description TEXT,
    prep_time INTEGER,
    portions INTEGER NOT NULL DEFAULT 1,
    rating DOUBLE PRECISION,
    ratings_count INTEGER,
    author_id VARCHAR(255)
);

CREATE TABLE ingredients (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE comments (
    id VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    text TEXT NOT NULL,
    recipe_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL
);

CREATE TABLE favorite_recipes (
    user_id VARCHAR(255),
    recipe_id VARCHAR(255),
    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE recipe_ingredients (
    recipe_id VARCHAR(255),
    ingredient VARCHAR(255),
    quantity DOUBLE PRECISION,
    unit VARCHAR(255)
);

CREATE TABLE recipe_instructions (
    recipe_id VARCHAR(255),
    instructions VARCHAR(255)
);

CREATE TABLE user_ratings (
    user_id VARCHAR(255),
    recipe_id VARCHAR(255),
    rating INTEGER,
    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE email_verification_tokens (
    id VARCHAR(255) PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP NOT NULL
);

ALTER TABLE recipes
    ADD CONSTRAINT fk_recipes_author
        FOREIGN KEY (author_id) REFERENCES users(id);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE;

ALTER TABLE favorite_recipes
    ADD CONSTRAINT fk_favorite_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE favorite_recipes
    ADD CONSTRAINT fk_favorite_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE;

ALTER TABLE recipe_ingredients
    ADD CONSTRAINT fk_recipe_ingredients_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE;

ALTER TABLE recipe_instructions
    ADD CONSTRAINT fk_recipe_instructions_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE;

ALTER TABLE user_ratings
    ADD CONSTRAINT fk_user_ratings_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE user_ratings
    ADD CONSTRAINT fk_user_ratings_recipe
        FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE;

ALTER TABLE email_verification_tokens
    ADD CONSTRAINT fk_email_verification_tokens_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;