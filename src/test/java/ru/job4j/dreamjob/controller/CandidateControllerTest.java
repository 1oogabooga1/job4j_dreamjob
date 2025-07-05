package ru.job4j.dreamjob.controller;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.SimpleCandidateService;
import ru.job4j.dreamjob.service.SimpleCityService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

class CandidateControllerTest {

    private CandidateController controller;

    private CandidateService candidateService;

    private CityService cityService;

    private MultipartFile testFile;

    @BeforeEach
    void initServices() {
        candidateService = mock(SimpleCandidateService.class);
        cityService = mock(SimpleCityService.class);
        controller = new CandidateController(candidateService, cityService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});

    }

    @Test
    void whenGetAllIsSuccessful() {
        var firstCandidate = new Candidate(1, "first name", "first description", now(), 1, 1);
        var secondCandidate = new Candidate(1, "second name", "second description", now(), 2, 2);
        var expectedCandidates = List.of(firstCandidate, secondCandidate);
        when(candidateService.findAll()).thenReturn(expectedCandidates);

        var model = new ConcurrentModel();
        var view = controller.getAll(model);
        var actualCandidates = model.getAttribute("candidates");

        assertThat(view).isEqualTo("candidates/list");
        assertThat(expectedCandidates).isEqualTo(actualCandidates);
    }

    @Test
    void whenGetCreationPageIsSuccessful() {
        var firstCity = new City(1, "Moscow");
        var secondCity = new City(2, "Saint-P");
        var expectedCities = List.of(firstCity, secondCity);
        when(cityService.findAll()).thenReturn(expectedCities);

        var model = new ConcurrentModel();
        var view = controller.getCreationPage(model);
        var actualCities = model.getAttribute("cities");

        assertThat(view).isEqualTo("candidates/create");
        assertThat(actualCities).isEqualTo(expectedCities);
    }

    @Test
    void whenCreateCandidateIsSuccessful() throws IOException {
        var firstCandidate = new Candidate(1, "first name", "first description", now(), 1, 1);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.save(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(firstCandidate);

        var model = new ConcurrentModel();
        var view = controller.create(firstCandidate, testFile, model);
        var actualCandidate = candidateArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(firstCandidate);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    void whenCreateCandidateIsUnsuccessful() {
        var expectedException = new RuntimeException("Failed to write file");
        when(candidateService.save(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = controller.create(new Candidate(), testFile, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    void whenGetByIdIsSuccessful() {
        var firstCandidate = new Candidate(1, "first name", "first description", now(), 1, 1);
        var cities = List.of(new City(1, "Moscow"), new City(2, "Saint-P"));
        when(candidateService.findById(firstCandidate.getId())).thenReturn(Optional.of(firstCandidate));
        when(cityService.findAll()).thenReturn(cities);

        var model = new ConcurrentModel();
        var view = controller.getById(model, firstCandidate.getId());
        var actualCandidate = model.getAttribute("candidate");
        var actualCities = model.getAttribute("cities");

        assertThat(view).isEqualTo("candidates/one");
        assertThat(actualCandidate).isEqualTo(firstCandidate);
        assertThat(actualCities).isEqualTo(cities);
    }

    @Test
    void whenGetByIdIsUnsuccessful() {
        when(candidateService.findById(any(Integer.class))).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = controller.getById(model, 1);
        var actualMsg = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMsg).isEqualTo("Кандидат с указанным идентификатором не найдена");
    }

    @Test
    void whenUpdateSuccessful() throws IOException {
        var firstCandidate = new Candidate(1, "first name", "first description", now(), 1, 1);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var candidateArgument = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgument = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.update(candidateArgument.capture(), fileDtoArgument.capture())).thenReturn(true);

        var model = new ConcurrentModel();
        var view = controller.update(firstCandidate, testFile, model);
        var actualCandidate = candidateArgument.getValue();
        var actualFile = fileDtoArgument.getValue();

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(firstCandidate);
        assertThat(actualFile).usingRecursiveComparison().isEqualTo(fileDto);
    }

    @Test
    void whenUpdateUnsuccessfulThenErrorAndMessage() {
        when(candidateService.update(any(), any())).thenReturn(false);

        var model = new ConcurrentModel();
        var view = controller.update(new Candidate(), testFile, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Кандидат не найден");
    }

    @Test
    void whenUpdateUnsuccessfulThenErrorAndThrowsException() {
        when(candidateService.update(any(), any())).thenThrow(new RuntimeException("some exception occured"));

        var model = new ConcurrentModel();
        var view = controller.update(new Candidate(), testFile, model);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("some exception occured");
    }

    @Test
    void whenDeleteIsSuccessful() {
        when(candidateService.deleteById(any(Integer.class))).thenReturn(true);

        var view = controller.delete(new ConcurrentModel(), 1);

        assertThat(view).isEqualTo("redirect:/candidates");
    }

    @Test
    void whenCandidateIsNotFoundThenDeleteFailed() {
        when(candidateService.deleteById(any(Integer.class))).thenReturn(false);

        var model = new ConcurrentModel();
        var view = controller.delete(model, 1);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo("Кандидат с указанным идентификатором не найдена");
    }
}