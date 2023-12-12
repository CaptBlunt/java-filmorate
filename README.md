![ER diagram к проекту filmorate и запросы для основных операций приложения.](ER%20diagram%20filmorate.png)

- Вывести всю информацию о первых 100 пользователей
```
SELECT *
FROM users
LIMIT 100;
```
- Вывести пользователя с id 100
```
SELECT *
FROM users
WHERE user_id=100;
```

- Вывести название, жанр, рейтинг и дату выхода фильма в промежутке с 2000 поо 2005 годы
```
SELECT name_film, 
genre.name_genre, 
mpa.name_mpa, release_date 
FROM films
JOIN genre ON films.genre_id=genre.genre_id
JOIN mpa ON films.mpa_id=mpa.mpa_id
WHERE EXTRACT(YEAR FROM CAST(release_date)) BETWEEN 2000 AND 2005;
```

- Вывести название, дату выхода и количество лайков первых 10 популярных фильмов 
```
SELECT f.name_film, 
f.release_film, 
COUNT(u_l.film_id) AS like_count
FROM films AS f
JOIN user_likes AS u_l ON f.user_id=u_l.user_id
GROUP BY f.name_film
ORDER BY like_count DESC
LIMIT 10;
```


