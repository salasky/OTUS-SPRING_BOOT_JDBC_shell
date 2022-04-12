DROP TABLE IF EXISTS authors;
CREATE TABLE authors
(
    author_id   BIGINT PRIMARY KEY auto_increment,
    author_name VARCHAR(255)
);

DROP TABLE IF EXISTS genres;
CREATE TABLE genres
(
    genre_id   BIGINT PRIMARY KEY auto_increment,
    genre_name VARCHAR(255)
);

DROP TABLE IF EXISTS books;
CREATE TABLE books
(
    book_id   BIGINT PRIMARY KEY auto_increment,
    book_name VARCHAR(255),
    author_id BIGINT,
    genre_id  BIGINT,
    foreign key (author_id) references authors(author_id),
    foreign key (genre_id) references genres(genre_id)
);
