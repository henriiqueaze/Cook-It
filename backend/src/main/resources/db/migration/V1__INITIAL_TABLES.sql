CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    photo VARCHAR(255)
);

CREATE TABLE recipes (
    id VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP,
    image VARCHAR(255),
    name VARCHAR(255),
    prep_time INTEGER,
    rating DOUBLE PRECISION,
    ratings_count INTEGER,
    author_id VARCHAR(255)
);

CREATE TABLE ingredients (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE comments (
    id VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP,
    text VARCHAR(255),
    recipe_id VARCHAR(255),
    user_id VARCHAR(255)
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

ALTER TABLE recipes
ADD CONSTRAINT fk_recipes_author
FOREIGN KEY (author_id) REFERENCES users(id);

ALTER TABLE comments
ADD CONSTRAINT fk_comments_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE comments
ADD CONSTRAINT fk_comments_recipe
FOREIGN KEY (recipe_id) REFERENCES recipes(id);

ALTER TABLE favorite_recipes
ADD CONSTRAINT fk_favorite_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE favorite_recipes
ADD CONSTRAINT fk_favorite_recipe
FOREIGN KEY (recipe_id) REFERENCES recipes(id);

ALTER TABLE recipe_ingredients
ADD CONSTRAINT fk_recipe_ingredients_recipe
FOREIGN KEY (recipe_id) REFERENCES recipes(id);

ALTER TABLE recipe_instructions
ADD CONSTRAINT fk_recipe_instructions_recipe
FOREIGN KEY (recipe_id) REFERENCES recipes(id);

ALTER TABLE user_ratings
ADD CONSTRAINT fk_user_ratings_user
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_ratings
ADD CONSTRAINT fk_user_ratings_recipe
FOREIGN KEY (recipe_id) REFERENCES recipes(id);