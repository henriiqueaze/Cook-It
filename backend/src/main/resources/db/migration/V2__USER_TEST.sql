INSERT INTO users (id, email, name, password, photo, email_verified)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'teste@cookit.com',
    'Usuário Teste',
    '$2a$10$PuC02urGBQKG7ivwJC7hcOjEdgrIgmZLy46RBGn.4Pa.BZTNL8dL.',
    null,
    true
);

INSERT INTO recipes (
    id,
    created_at,
    image,
    name,
    description,
    prep_time,
    portions,
    rating,
    ratings_count,
    author_id
)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    CURRENT_TIMESTAMP,
    null,
    'Receita de Teste',
    'Essa é uma receita de teste para validar o sistema.',
    30,
    2,
    0.0,
    0,
    '11111111-1111-1111-1111-111111111111'
);

INSERT INTO ingredients (id, name)
VALUES (
    '33333333-3333-3333-3333-333333333333',
    'Farinha de trigo'
);

INSERT INTO recipe_ingredients (recipe_id, ingredient, quantity, unit)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    'Farinha de trigo',
    2,
    'xícara'
);

INSERT INTO recipe_instructions (recipe_id, instructions)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    'Misture todos os ingredientes até formar uma massa homogênea.'
);

INSERT INTO comments (id, created_at, text, recipe_id, user_id)
VALUES (
    '44444444-4444-4444-4444-444444444444',
    CURRENT_TIMESTAMP,
    'Comentário de teste para validar os relacionamentos.',
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111'
);