package ru.otus.console;

import org.apache.commons.lang3.StringUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.CollectionUtils;
import ru.otus.service.BookService;

@ShellComponent
public class UserCommand {
    private static final String ERROR_MESSAGE_PREFIX = "Ошибка при выполении команды: ";

    private final BookService bookService;

    public UserCommand(BookService bookService) {
        this.bookService = bookService;
    }

    @ShellMethod("Найти книгу")
    public String find(
            @ShellOption(defaultValue = "0") long id,
            @ShellOption(defaultValue = "") String name) {
        if (id > 0) {
            final var optionalBook = bookService.findById(id);
            if (optionalBook.isEmpty()) {
                return ERROR_MESSAGE_PREFIX + "не найдена книга с id " + id;
            }
            return optionalBook.get().toString();
        } else if (StringUtils.isNoneEmpty(name)) {
            final var books = bookService.findByName(name);
            if (CollectionUtils.isEmpty(books)) {
                return ERROR_MESSAGE_PREFIX + "не найдены книги с name " + name;
            }
            return books.toString();
        }
        return ERROR_MESSAGE_PREFIX + "необходимо задать id или имя книги";
    }

    /**
     * Для создания в консоли используем следующую запись:
     * <p>create 'Сказка о рыбаке и рыбке' 'А.С. Пушкин' 'Сказка'</p>
     * Предполагаем, что у нас один автор и один жанр у книги
     */
    @ShellMethod("Создать книгу")
    public String create(String name, String authorName, String genreName) {
        try {
            return bookService.create(name, authorName, genreName).toString();
        } catch (Exception ex) {
            return ERROR_MESSAGE_PREFIX + ex.getMessage();
        }
    }

    @ShellMethod("Обновить книгу")
    public String update(
            long id,
            String name,
            @ShellOption(defaultValue = "") String authorName,
            @ShellOption(defaultValue = "") String genreName) {
        try {
            return bookService.update(id, name, authorName, genreName).toString();
        } catch (Exception ex) {
            return ERROR_MESSAGE_PREFIX + ex.getMessage();
        }
    }

    @ShellMethod("Удалить книгу")
    public String delete(@ShellOption(defaultValue = "0") long id) {
        if (id == 0) {
            return ERROR_MESSAGE_PREFIX + "необходимо задать id книги";
        }
        bookService.delete(id);
        return "done.";
    }
}
