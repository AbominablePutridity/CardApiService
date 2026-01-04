// Скрипт, хранящий функции для передачи запросов и получения ответов от Backend приложения

const apiRoute = "http://localhost:8081/"; // роут нашего бэка, откуда будем брать данные

// 1. GET запрос - ПРОЩЕ НЕКУДА (AJAX - jQuery)
/**
 * Get-запрос на взятие данных по заголовку с токеном и параметрами.
 * @param headerToken токен пользователя.
 * @param endpoint роут с параметрами, из которого берем данные.
 */
function dataGet(headerToken, endpoint) {
    return $.ajax({
        url: apiRoute + endpoint,
        type: 'GET',
        headers: {
            'Authorization': `Bearer ${headerToken}`,
            'X-Api-Token': `Bearer ${headerToken}`,
        },
        dataType: 'json' // jQuery сам распарсит JSON
    });
}

// 2. POST запрос - ТОЖЕ ПРОСТО (AJAX - jQuery)
/**
 * Post-запрос на взятие данных по заголовку с токеном и параметрами.
 * @param headerToken токен пользователя.
 * @param endpoint роут с параметрами, из которого берем данные.
 * @param jsonData тезо запроса для передачи данных в формате json.
 */
function dataPost(headerToken, endpoint, jsonData = {}) {
    return $.ajax({
        url: apiRoute + endpoint,
        type: 'POST',
        headers: {
            'Authorization': `Bearer ${headerToken}`,
            'X-Api-Tocken': `Bearer ${headerToken}`,
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(jsonData), // Отправляем как JSON
        dataType: 'text' // Ожидаем текст (JWT токен)
    });
}
