package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MpaService implements MpaStorage {
    private final MpaDao mpaDao;

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    @Override
    public Mpa getMpaById(int id) {
        return mpaDao.getMpaById(id);
    }
}
