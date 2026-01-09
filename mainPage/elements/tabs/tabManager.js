// Скрипт, отрисоввывающий на вкладках информацию о картах

// Находим элемент
let div = document.getElementById('screens');
let currentCardId = null; //для взятия данных о конкретной карте по ее Id-коду

/**
 * для взятия значения Id карты.
 */
function getCurrentCardId() {
    return currentCardId;
}

/**
 * Для установки какого либо действия над данными конкретной карты, задаем ее Id - для взятия по нему данных.
 * @param cardId Id-код карты.
 */
function setCurrentCardId(cardId) {
    console.log("CurrentCardId = ", cardId);
    currentCardId = cardId;
}

// переменная, хранящая массив с данными как результат выполнения запроса
let dataForRendering = null;

/**
 * Отрисоввывает информацию о каждой карте на вкладке "О карте" по данным карты
 * @param data Обьект с данными о карте для отрисовки в формате json
 */
function setTabForCardData(data)
{
    console.log("setTabForCardData-> ", data);

    // Вставляем любой HTML
    div.innerHTML = 
    `
    <h3>${data.number}</h3></br>
    <p>
      ${data.statusCardDto.name}</br>
      ${data.userDto.surname} ${data.userDto.name} ${data.userDto.patronymic}</br>
      ${data.balance} ${data.currencyDto.sign}</br>
      Заблокированна ли: ${data.isBlocked}</br>
      <button>Совершить перевод</button>
    </p>
    `;
}

/**
 * Функция со сбором данных о транзакциях, и в дальнейшем, ее отрисовки.
 * @param cardId Id карты, у которой берем все транзакции.
 * @param page Страница (для пагинации).
 * @param size Количество элементов на странице (для пагинации).
 */
function setTabForCardTranzactions(cardId, page, size)
{
    //берем данные о текущем пользователе из API
    dataGet(
        sessionStorage.getItem('jwtToken'), // берем наш токен из сессии
        `api/cardTransfer/user/getAllTransfersToUserCard?page=${page}&size=${size}&idUserCard=${cardId}`,
        
    ).done(function(data) {
        // ДАННЫЕ ЗДЕСЬ!
        console.log("All tranzactions = ", data);

        dataForRendering = data; //приравниваем данные из api в массив dataForRendering, из которых будем брать данные о транзакциях

    }).fail(function() {
        alert("Shown transactions failed!");
    });
}
