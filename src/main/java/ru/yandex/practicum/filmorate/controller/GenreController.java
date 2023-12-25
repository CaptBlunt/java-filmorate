package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable("id") int genreId) {
        log.info("Пришёл GET запрос /genres/" + genreId);
        Genre genre = genreService.getGenreById(genreId);
        log.info("Отправлен ответ getGenreById/genres/" + genreId + " с телом : {}", genre);
        return ResponseEntity.ok(genre);
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        log.info("Пришёл GET запрос /genres");
        List<Genre> genres = genreService.getAllGenres();
        log.info("Отправлен ответ getGenreById/genres с телом {}", genres);
        return ResponseEntity.ok(genres);
    }
}
