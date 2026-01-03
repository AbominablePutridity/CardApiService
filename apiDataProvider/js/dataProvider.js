const apiRoute = "http://localhost:8081/";

// 1. GET запрос - ПРОЩЕ НЕКУДА
function dataGet(headerToken, endpoint, params = {}) {
    return $.ajax({
        url: apiRoute + endpoint,
        type: 'GET',
        data: params, // jQuery сам превратит в query string
        headers: {
            'Authorization': `Bearer ${headerToken}`
        },
        dataType: 'json' // jQuery сам распарсит JSON
    });
}

// 2. POST запрос - ТОЖЕ ПРОСТО
function dataPost(headerToken, endpoint, jsonData = {}) {
    return $.ajax({
        url: apiRoute + endpoint,
        type: 'POST',
        headers: {
            'Authorization': `Bearer ${headerToken}`,
            'Content-Type': 'application/json'
        },
        data: JSON.stringify(jsonData), // Отправляем как JSON
        dataType: 'text' // Ожидаем текст (JWT токен)
    });
}

// 3. Даже проще - без функций вообще, прямо в коде:
// function login() {
//     $.ajax({
//         url: apiRoute + 'api/users/guest/getEntryTocken',
//         type: 'GET',
//         data: {
//             login: $('#login').val(),
//             password: $('#password').val()
//         },
//         success: function(token) {
//             console.log("Token:", token);
//             localStorage.setItem('jwtToken', token);
//             window.location.href = 'cards.html';
//         },
//         error: function() {
//             alert("Login failed!");
//         }
//     });
// }
