DROP TABLE IF EXISTS users,events,category,request,compilation,compilation_events cascade;
CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email varchar(255) UNIQUE NOT NULL,
    name  varchar(255)       NOT NULL
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         TEXT                                   NOT NULL,
    category_id        BIGINT REFERENCES CATEGORY (id) ON DELETE CASCADE NOT NULL,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        TEXT,
    event_date         TIMESTAMP WITHOUT TIME ZONE                       NOT NULL,
    initiator_id       BIGINT REFERENCES USERS (id) ON DELETE CASCADE,
    lat                float,
    lon                float,
    paid               boolean,
    participant_limit  BIGINT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation boolean,
    state              varchar(255),
    title              varchar(255),
    views              int
);

CREATE TABLE IF NOT EXISTS request
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY,
    event_id     BIGINT REFERENCES events (id),
    requester_id BIGINT REFERENCES users (id),
    created      TIMESTAMP WITHOUT TIME ZONE,
    state        varchar(255)
);

CREATE TABLE IF NOT EXISTS compilation
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned boolean,
    title  varchar(255)
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id BIGINT REFERENCES compilation (id),
    events_id      BIGINT REFERENCES events (id)
);