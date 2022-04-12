package ru.otus.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.entity.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookRowMapper implements RowMapper<Book> {
    private final AuthorRowMapper authorRowMapper;
    private final GenreRowMapper genreRowMapper;

    public BookRowMapper(AuthorRowMapper authorRowMapper, GenreRowMapper genreRowMapper) {
        this.authorRowMapper = authorRowMapper;
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        final var book = new Book();
        book.setId(resultSet.getLong("book_id"));
        book.setName(resultSet.getString("book_name"));
        book.setAuthor(authorRowMapper.mapRow(resultSet, i));
        book.setGenre(genreRowMapper.mapRow(resultSet, i));
        return book;
    }
}
