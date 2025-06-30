package ru.job4j.dreamjob.repository;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.List;
import java.util.Properties;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oUserRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (User user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    void whenSaveThenGetSame() {
        var user = sql2oUserRepository
                .save(new User(0, "email", "name", "password"));
        var savedUser = sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void whenSaveSeveralThenGetThemAll() {
        var firstUser = sql2oUserRepository.save(new User(0, "first email", "first name", "first password")).get();
        var secondUser = sql2oUserRepository.save(new User(0, "second email", "second name", "second password")).get();
        var thirdUser = sql2oUserRepository.save(new User(0, "third email", "third name", "third password")).get();

        var result = sql2oUserRepository.findAll();
        assertThat(result).isEqualTo(List.of(firstUser, secondUser, thirdUser));
    }

    @Test
    void whenNothingSaveThenNothingGet() {
        assertThat(sql2oUserRepository.findAll()).isEqualTo(emptyList());
    }

    @Test
    void whenDeleteThenGetNothing() {
        var user = sql2oUserRepository.save(new User(0, "email", "name", "password"));
        var isDeleted = sql2oUserRepository.deleteById(user.get().getId());
        assertThat(isDeleted).isTrue();
        assertThat(sql2oUserRepository.findByEmailAndPassword(user.get().getEmail(), user.get().getPassword())).isEqualTo(empty());
    }

    @Test
    void whenFindByInvalidPasswordOrEmailThenGetNothing() {
        var user = sql2oUserRepository.save(new User(0, "email", "name", "password"));
        assertThat(sql2oUserRepository.findByEmailAndPassword("email", "invalid password")).isEmpty();
        assertThat(sql2oUserRepository.findByEmailAndPassword("invalid email", "password")).isEmpty();
    }

    @Test
    void whenEmailAlreadyExistsThenThrowsException() {
        var firstUser = sql2oUserRepository.save(new User(0, "email", "name", "password"));
        assertThrows(Sql2oException.class, () -> {
            sql2oUserRepository.save(new User(1, "email", "another name", "another password"));
        });
    }
}