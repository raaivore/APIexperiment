CREATE TABLE mushrooms (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP,
    location GEOMETRY(Point, 4326),
    description TEXT
);

CREATE TABLE addmushrooms (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP,
    features JSONB
);

CREATE TABLE updatemushrooms (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP,
    features JSONB,
    mushroomid INT
);

CREATE TABLE querymushrooms (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP,
    features JSONB
);

CREATE TABLE deletemushrooms (
    id SERIAL PRIMARY KEY,
    timestamp TIMESTAMP,
    mushroomid INT
);

