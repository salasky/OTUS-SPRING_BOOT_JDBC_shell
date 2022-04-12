package ru.otus.dao.impl;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.entity.Author;
import ru.otus.entity.Book;
import ru.otus.entity.Genre;
import ru.otus.mapper.AuthorRowMapper;
import ru.otus.mapper.BookRowMapper;
import ru.otus.mapper.GenreRowMapper;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с книгами")
@JdbcTest
@Import({BookDAOJdbc.class, BookRowMapper.class, AuthorRowMapper.class, GenreRowMapper.class})
class BookDAOImplTest {
    private static final long ID = 1;
    private static final String NAME = "Песнь льда и Пламени";
    private static final Author AUTHOR = new Author(2, "Дж.Дж. Мартин");
    private static final Genre GENRE = new Genre(2, "Фэнтези");
    private static final Book TEST_BOOK = new Book(ID, NAME, AUTHOR, GENRE);

    @Autowired
    private BookDAOJdbc bookDAO;


    @DisplayName("Поиск по ID")
    @Test
    void findById() {
        final var book = bookDAO.findById(ID).orElseThrow();
        assertThat(TEST_BOOK).usingRecursiveComparison().isEqualTo(book);
    }

    @DisplayName("Поиск по названию книги")
    @Test
    void findByName() {
        final var book = bookDAO.findByName(NAME).get(0);
        assertThat(TEST_BOOK).usingRecursiveComparison().isEqualTo(book);
    }


    @DisplayName("Сохранение книги")
    @Test
    void save() {
        long newId = 2;
        final var newBook = new Book(newId, "Игра Престолов", AUTHOR, GENRE);

        assertThat(bookDAO.findById(newId).isPresent()).isFalse();

        bookDAO.save(newBook);

        final var savedBook = bookDAO.findById(newId).orElseThrow();
        assertThat(savedBook).usingRecursiveComparison().isEqualTo(newBook);
    }


    @DisplayName("Обновление книги")
    @Test
    void update() {
        String newName = "Игра Престолов";
        final var newBook = new Book(ID, newName, AUTHOR, GENRE);

        var savedBook = bookDAO.findById(ID).orElseThrow();
        assertThat(savedBook).usingRecursiveComparison().isNotEqualTo(newBook);

        bookDAO.update(newBook);

        savedBook = bookDAO.findById(ID).orElseThrow();
        assertThat(savedBook).usingRecursiveComparison().isEqualTo(newBook);
    }


    @DisplayName("Удаление книги по ID")
    @Test
    void delete() {
        assertThat(bookDAO.findById(ID).isPresent()).isTrue();
        bookDAO.delete(ID);
        assertThat(bookDAO.findById(ID).isPresent()).isFalse();
    }
}