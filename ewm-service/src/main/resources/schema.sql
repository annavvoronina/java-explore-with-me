DROP TABLE IF EXISTS users, events, category, request, compilation, compilation_events, subscriptions CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email               VARCHAR(255) UNIQUE NOT NULL,
    name                VARCHAR(255)        NOT NULL
);

CREATE TABLE IF NOT EXISTS category
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation          TEXT NOT NULL,
    category_id         BIGINT REFERENCES category (id) ON DELETE CASCADE NOT NULL,
    created_on          TIMESTAMP WITHOUT TIME ZONE,
    description         TEXT,
    event_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id        BIGINT REFERENCES users (id) ON DELETE CASCADE,
    lat                 FLOAT,
    lon                 FLOAT,
    paid                BOOLEAN,
    participant_limit   BIGINT,
    published_on        TIMESTAMP WITHOUT TIME ZONE,
    request_moderation  BOOLEAN,
    state               VARCHAR(255),
    title               VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS request
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY,
    event_id            BIGINT REFERENCES events (id) ON DELETE NO ACTION NOT NULL,
    requester_id        BIGINT REFERENCES users (id) ON DELETE NO ACTION NOT NULL,
    created             TIMESTAMP WITHOUT TIME ZONE,
    status              VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS compilation
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned              BOOLEAN,
    title               VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    compilation_id      BIGINT REFERENCES compilation (id) ON DELETE NO ACTION NOT NULL,
    events_id           BIGINT REFERENCES events (id) ON DELETE NO ACTION NOT NULL
);

CREATE TABLE IF NOT EXISTS subscriptions
(
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id             BIGINT,
    subscriber_id       BIGINT,
    status              VARCHAR(30)
);