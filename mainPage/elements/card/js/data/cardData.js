//Скрипт, хранящий данные о картах пользователя и вызов отрисовки этих данных
//(для изначального получения списка карт)
//(для изначальной загрузки карт по пагинации)

// Настройки пагинации
let currentPage = 0;      // Текущая страница
let itemsPerPage = 3;     // Сколько карточек выводить на одной странице

let cardsData = []; // Здесь будут наши данные карт текущего пользователя

//берем данные о картах нашего пользователя из API
dataGet(
    sessionStorage.getItem('jwtToken'), // берем наш токен из сессии
    `api/card/user/getAllCards?page=${currentPage}&size=${itemsPerPage}&isHideCardNumber=true`,
    
  ).done(function(data) {
    // ДАННЫЕ ЗДЕСЬ!
    console.log("The data = ", data);

    cardsData = data; //приравниваем данные из api в массив cardsData, из которых будем брать данные о картах пользователя на отрисовку

    // Вызов функции для откисовки (карт) полученых данных из текущего запроса на HTML страницу
    // (т.к. блок .done - область видимости полученых данных из запроса)
    renderCards(currentPage+1);
  }).fail(function() {
    alert("Login failed!");
});

/*[
 {
    balance: 1000,
    currencyDto: {
      "id": 2,
      "name": "Доллар США",
      "sign": "$"
    },
    id: 4,
    isBlocked: false,
    number: "**** **** **** 4444",
    statusCardDto: {
      id: 1,
      name: "Активна"
    },
    userDto: {
      id: 1,
      isBlocked: null,
      login: "ivanov",
      name: "Иван",
      patronymic: "Иванович",
      role: "USER",
      surname: "Иванов"
    },
    validityPeriod: "2027-05-31"
  },
  {
    balance: 12001,
    currencyDto: {
      id: 1,
      name: "Российский рубль",
      sign: "₽"
    },
    id: 1,
    isBlocked: false,
    number: "**** **** **** 1111",
    statusCardDto: {
      id: 1,
      name: "Активна"
    },
    userDto: {
      id: 1,
      isBlocked: null,
      login: "ivanov",
      name: "Иван",
      patronymic: "Иванович",
      role: "USER",
      surname: "Иванов"
    },
    validityPeriod: "2026-12-31"
  },
  {
    balance: 0,
    currencyDto: {
      id: 1,
      name: "Российский рубль",
      sign: "₽"
    },
    id: 9,
    isBlocked: true,
    number: "string125",
    statusCardDto: {
      id: 1,
      name: "Активна"
    },
    userDto: {
      id: 1,
      isBlocked: null,
      login: "ivanov",
      name: "Иван",
      patronymic: "Иванович",
      role: "USER",
      surname: "Иванов"
    },
    validityPeriod: "2025-11-28"
  }
]*/
