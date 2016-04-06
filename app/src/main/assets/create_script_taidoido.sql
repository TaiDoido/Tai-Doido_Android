CREATE TABLE IF NOT EXISTS tb_posts (
    _id                 TEXT NOT NULL,
    title               TEXT NOT NULL,
    author              TEXT NOT NULL,
    last_update         TEXT NOT NULL,
    content             TEXT NOT NULL,
    url                 TEXT NOT NULL,
    image               TEXT,
    favorite            INTEGER DEFAULT 0
 );
