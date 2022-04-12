package ru.otus.dao.impl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.otus.dao.AuthorDAO;
import ru.otus.entity.Author;
import ru.otus.mapper.AuthorRowMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorDAOJdbc implements AuthorDAO {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;
    private final AuthorRowMapper authorRowMapper;

    public AuthorDAOJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations, AuthorRowMapper authorRowMapper) {
        this.namedParameterJdbcOperations = namedParameterJdbcOperations;
        this.authorRowMapper = authorRowMapper;
    }

    @Override
    public Optional<Author> findByName(String name) {
         String FIND_BY_NAME ="select author_id, author_name from authors where author_name = :name";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        final var authors = namedParameterJdbcOperations.query(FIND_BY_NAME, params, authorRowMapper);
        if (CollectionUtils.isEmpty(authors)) {
            return Optional.empty();
        }
        return Optional.ofNullable(authors.get(0));
    }
}
