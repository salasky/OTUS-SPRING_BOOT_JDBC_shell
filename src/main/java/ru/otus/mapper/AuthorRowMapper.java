package ru.otus.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.entity.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorRowMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        final var author = new Author();
        author.setId(resultSet.getLong("author_id"));
        author.setName(resultSet.getString("author_name"));
        return author;
    }
}
