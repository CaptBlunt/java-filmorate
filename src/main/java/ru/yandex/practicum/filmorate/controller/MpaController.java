package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable("id") int mpaId) {
        log.info("Пришёл GET запрос /mpa/" + mpaId);
        Mpa mpa = mpaService.getMpaById(mpaId);
        log.info("Отправлен ответ getMpaById/mpa/" + mpaId + " с телом {}", mpa);
        return mpa;
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        log.info("Пришёл GET запрос /mpa");
        List<Mpa> mpa = mpaService.getAllMpa();
        log.info("Отправлен ответ getAllMpa/mpa с телом {}", mpa);
        return mpa;
    }
}
