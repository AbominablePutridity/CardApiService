let userData = null; // Здесь будут наши данные карт текущего пользователя

//берем данные о текущем пользователе из API
dataGet(
    sessionStorage.getItem('jwtToken'), // берем наш токен из сессии
    `api/users/user/getCurrentUserObject`,
    
  ).done(function(data) {
    // ДАННЫЕ ЗДЕСЬ!
    console.log("The data = ", data);

    userData = data; //приравниваем данные из api в массив cardsData, из которых будем брать данные о картах пользователя на отрисовку

    // Вызов функции для вставки данных из массива на страницу
    // (т.к. блок .done - область видимости полученых данных из запроса)
    setUserData();
  }).fail(function() {
    alert("Login failed!");

    //если не смогли получить данные о текущем пользователе из API,
    // то это значит, что пользователь не вошел и не получил свой токен - перенаправляем на страницу входа!
    window.location.href = "../loginPage/login.html";
});

//const userData =
// {
//   "id": 1,
//   "isBlocked": null,
//   "login": "ivanov",
//   "name": "Иван",
//   "patronymic": "Иванович",
//   "role": "USER",
//   "surname": "Иванов"
// };
