package ru.otus.dao.impl;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.otus.dao.BookDAO;
import ru.otus.entity.Book;
import ru.otus.mapper.BookRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookDAOJdbc implements BookDAO {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final BookRowMapper bookRowMapper;

    public BookDAOJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, BookRowMapper bookRowMapper) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.bookRowMapper = bookRowMapper;
    }

    @Override
    public Optional<Book> findById(long id) {
        String FIND_BY_ID =
                "select b.book_id" +
                 ", b.book_name" +
                 ", b.author_id" +
                 ", b.genre_id" +
                 ", a.author_id" +
                 ", a.author_name" +
                 ", g.genre_name from books b " +
                 "inner join authors a on b.author_id = a.author_id " +
                 "inner join genres g on b.genre_id = g.genre_id " +
                 "where b.book_id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getOne(
                namedParameterJdbcOperations.query(FIND_BY_ID, params, bookRowMapper)
        );
    }

    @Override
    public List<Book> findByName(String name) {
        String FIND_BY_NAME =
                "select b.book_id" +
                ", b.book_name" +
                ", b.author_id" +
                ", b.genre_id" +
                ", a.author_id" +
                ", a.author_name" +
                ", g.genre_name from books b " +
                "inner join authors a on b.author_id = a.author_id " +
                "inner join genres g on b.genre_id = g.genre_id " +
                "where b.book_name = :name";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        return namedParameterJdbcOperations.query(FIND_BY_NAME, params, bookRowMapper);
    }

    @Override
    public long save(Book book) {
        String INSERT = "insert into books(book_name, author_id, genre_id) values (:name, :author_id, :genre_id)";
        Map<String, Object> params = paramsByBook(book);
        final var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update(
                INSERT,
                new MapSqlParameterSource(params),
                keyHolder
        );
        final var key = keyHolder.getKeyAs(Long.class);
        return key == null ? 0 : key;
    }

    @Override
    public void update(Book book) {
        String UPDATE_PARAMS_BY_ID =
                "update books set book_name = :name, author_id = :author_id, genre_id = :genre_id where book_id = :id";
        Map<String, Object> params = paramsByBook(book);
        namedParameterJdbcOperations.update(
                UPDATE_PARAMS_BY_ID,
                params
        );
    }

    @Override
    public void delete(long id) {
        String DELETE_BY_ID = "delete from books where book_id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        namedParameterJdbcOperations.update(DELETE_BY_ID, params);
    }

    private Map<String, Object> paramsByBook(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("name", book.getName());
        params.put("author_id", book.getAuthor().getId());
        params.put("genre_id", book.getGenre().getId());
        return params;
    }

    private Optional<Book> getOne(List<Book> books) {
        if (CollectionUtils.isEmpty(books)) {
            return Optional.empty();
        }
        return Optional.ofNullable(books.get(0));
    }
}
