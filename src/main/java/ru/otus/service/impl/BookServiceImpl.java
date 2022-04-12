package ru.otus.service.impl;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.AuthorDAO;
import ru.otus.dao.BookDAO;
import ru.otus.dao.GenreDAO;
import ru.otus.entity.Book;
import ru.otus.exception.NotFoundException;
import ru.otus.service.BookService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final GenreDAO genreDAO;

    public BookServiceImpl(BookDAO bookDAO, AuthorDAO authorDAO, GenreDAO genreDAO) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.genreDAO = genreDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findByName(String name) {
        return bookDAO.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findById(long id) {
        return bookDAO.findById(id);
    }

    @Override
    @Transactional
    public Book create(String name, String authorName, String genreName) {
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(name),
                "имя книги не может быть пустым"
        );
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(authorName),
                "имя автора не может быть пустым"
        );
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(genreName),
                "имя жанра не может быть пустым"
        );
        final var author = authorDAO.findByName(authorName)
                .orElseThrow(() -> new NotFoundException("отсутствует автор с именем " + authorName));
        final var genre = genreDAO.findByName(genreName)
                .orElseThrow(() -> new NotFoundException("отсутствует жанр с именем " + genreName));
        final var id = bookDAO.save(new Book(name, author, genre));
        return bookDAO.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Не удалось сохранить книгу: имя=%s, автор=%s, жанр=%s",
                                name,
                                authorName,
                                genreName)
                )
        );
    }

    @Override
    @Transactional
    public Book update(long id, String name, String authorName, String genreName) {
        Preconditions.checkArgument(
                StringUtils.isNoneEmpty(name),
                "имя книги не может быть пустым"
        );
        final var book = bookDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Отсутствует книга с id=" + id));
        book.setName(name);
        if (StringUtils.isNoneEmpty(authorName) && !Objects.equals(book.getAuthor().getName(), authorName)) {
            final var author = authorDAO.findByName(authorName)
                    .orElseThrow(() -> new NotFoundException("отсутствует автор с именем " + authorName));
            book.setAuthor(author);
        }
        if (StringUtils.isNoneEmpty(genreName) && !Objects.equals(book.getGenre().getName(), genreName)) {
            final var genre = genreDAO.findByName(genreName)
                    .orElseThrow(() -> new NotFoundException("отсутствует жанр с именем " + genreName));
            book.setGenre(genre);
        }
        bookDAO.update(book);
        return bookDAO.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Не удалось обновить книгу: имя=%s, автор=%s, жанр=%s",
                                name,
                                authorName,
                                genreName)
                )
        );
    }

    @Override
    @Transactional
    public void delete(long id) {
        bookDAO.delete(id);
    }
}
