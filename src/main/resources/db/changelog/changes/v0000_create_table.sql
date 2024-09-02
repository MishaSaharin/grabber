CREATE TABLE IF NOT EXISTS post.post
(
    id      UUID      NOT NULL,
    name    TEXT      NOT NULL DEFAULT 'undefined',
    text    TEXT      NOT NULL DEFAULT 'undefined',
    link    TEXT      NOT NULL DEFAULT 'undefined',
    created TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT pk_module PRIMARY KEY (id)
);