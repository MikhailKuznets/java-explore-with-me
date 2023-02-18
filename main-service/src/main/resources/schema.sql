DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events_compilations CASCADE;
DROP TABLE IF EXISTS requests CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255)    NOT NULL,
    email   VARCHAR(255)    NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    category_name   VARCHAR(255)    NOT NULL,
    CONSTRAINT pr_category PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation  VARCHAR(2000)    NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published TIMESTAMP WITHOUT TIME ZONE,
    category_id    BIGINT REFERENCES categories ON DELETE CASCADE,
    description  VARCHAR(7000)    NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT REFERENCES users,
    location_lat FLOAT NOT NULL,
    location_lon FLOAT NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    confirmed_requests INTEGER NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    state  VARCHAR(30)    NOT NULL,
    title  VARCHAR(120)    NOT NULL,
--     views INTEGER NOT NULL,
    CONSTRAINT pr_event PRIMARY KEY (event_id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN NOT NULL,
    title  VARCHAR(120)    NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (compilation_id)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id BIGINT REFERENCES events (event_id),
    compilation_id BIGINT REFERENCES compilations (compilation_id),
    CONSTRAINT pk_events_compilations PRIMARY KEY (event_id, compilation_id)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT REFERENCES events,
    requester_id BIGINT REFERENCES users,
    status  VARCHAR(30)    NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (request_id)
);