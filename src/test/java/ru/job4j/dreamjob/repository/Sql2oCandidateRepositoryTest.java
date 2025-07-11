package ru.job4j.dreamjob.repository;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.File;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

class Sql2oCandidateRepositoryTest {

    private static Sql2oCandidateRepository sql2oCandidateRepository;

    private static Sql2oFileRepository sql2oFileRepository;

    private static File file;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oCandidateRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oCandidateRepository = new Sql2oCandidateRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = new File("test", "test");

        sql2oFileRepository.save(file);
    }

    @AfterAll
    public static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    public void clearVacancies() {
        var candidates = sql2oCandidateRepository.findAll();
        for (var candidate : candidates) {
            sql2oCandidateRepository.deleteById(candidate.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "Dmitrii", "Description",
                creationDate, 1, file.getId()));
        var savedCandidate = sql2oCandidateRepository.findById(candidate.getId()).get();
        assertThat(savedCandidate).usingRecursiveComparison().isEqualTo(candidate);
    }

    @Test
    public void whenSaveSeveralTimesThenGetALl() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var firstCandidate = sql2oCandidateRepository.save(new Candidate(0, "first", "first desc", creationDate, 1, file.getId()));
        var secondCandidate = sql2oCandidateRepository.save(new Candidate(0, "second", "second desc", creationDate, 2, file.getId()));
        var thirdCandidate = sql2oCandidateRepository.save(new Candidate(0, "third", "third desc", creationDate, 1, file.getId()));
        var fourthCandidate = sql2oCandidateRepository.save(new Candidate(0, "fourth", "fourth desc", creationDate, 3, file.getId()));
        var result = sql2oCandidateRepository.findAll();
        assertThat(result).isEqualTo(List.of(firstCandidate, secondCandidate, thirdCandidate, fourthCandidate));
    }

    @Test
    void whenNothingSaveThenNothingGet() {
        assertThat(sql2oCandidateRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oCandidateRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    void whenDeleteThenGetNothing() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "Dmitrii", "Description",
                creationDate, 1, file.getId()));
        var isDeleted = sql2oCandidateRepository.deleteById(candidate.getId());
        var savedCandidate = sql2oCandidateRepository.findById(candidate.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedCandidate).isEqualTo(empty());
    }

    @Test
    void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oCandidateRepository.deleteById(0)).isFalse();
    }

    @Test
    void whenUpdateThenGetUpdated() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = sql2oCandidateRepository.save(new Candidate(0, "Dmitrii", "Description",
                creationDate, 1, file.getId()));
        var updatedCandidate = new Candidate(candidate.getId(), "Sergey", "Some desc",
                creationDate.plusDays(1), 2, file.getId());
        var isUpdated = sql2oCandidateRepository.update(updatedCandidate);
        var savedCandidate = sql2oCandidateRepository.findById(updatedCandidate.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedCandidate).usingRecursiveComparison().isEqualTo(updatedCandidate);
    }

    @Test
    void whenUpdateInvalidCandidate() {
        var creationDate = now().truncatedTo(ChronoUnit.MINUTES);
        var candidate = new Candidate(0, "Dmitrii", "Description",
                creationDate, 1, file.getId());
        var isUpdated = sql2oCandidateRepository.update(candidate);
        assertThat(isUpdated).isFalse();
    }
}