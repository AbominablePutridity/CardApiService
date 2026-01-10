// Скрипт, отрисоввывающий на вкладках информацию о картах

let currentTranzCardId = null;
let currentTranzPage = 0;
let currentTranzSize = 10;
let currentTranzTotalPages = 0;

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
function setTabForCardTranzactions(cardId, page, size) {
    console.log(`Запрос: cardId=${cardId}, page=${page}, size=${size}`);
    
    dataGet(
        sessionStorage.getItem('jwtToken'),
        `api/cardTransfer/user/getAllTransfersToUserCard?page=${page}&size=${size}&idUserCard=${cardId}`,
    ).done(function(data) {
        console.log("Получены данные:", data);
        
        // ПРОСТО отрисовываем что получили
        div.innerHTML = 
        `
        <div class="items-container">
        ${data.map(item => `
            <div class="item">
            <h3 class="main-title">${item.description}</h3>
            <p class="info-text">
                <h1>${item.amountOfMoney} ${item.receiverDto?.currencyDto?.sign || ''}</h1>
            </p>
            <p class="info-details">
                Счет получателя: ${item.receiverDto?.number || 'Нет данных'} </br>
                Отправитель: ${item.senderDto?.userDto?.surname || ''} ${item.senderDto?.userDto?.name || ''} ${item.senderDto?.userDto?.patronymic || ''}</br>
                Получатель: ${item.receiverDto?.userDto?.surname || ''} ${item.receiverDto?.userDto?.name || ''} ${item.receiverDto?.userDto?.patronymic || ''}</br>
            </p>
            <span class="date">${item.transferDate}</span>
            </div>
            <hr>
        `).join('')}
        </div>
        
        <div class="pagination-container default-text default-panel">
            <button id="pag-tranz-button-back">← Назад</button>
            <span id="page-info"> Страница ${page + 1} </span>
            <button id="pag-tranz-button-forward">Вперед →</button>
        </div>
        `;
        
        // ВЕШАЕМ ОБРАБОТЧИКИ ПРЯМО СЕЙЧАС
        const btnBack = document.getElementById("pag-tranz-button-back");
        const btnForward = document.getElementById("pag-tranz-button-forward");
        
        // КНОПКА "НАЗАД" - запрос с page-1
        btnBack.onclick = function() {
            console.log("Нажата кнопка Назад!");
            setTabForCardTranzactions(cardId, page - 1, size);
        };
        
        // КНОПКА "ВПЕРЕД" - запрос с page+1
        btnForward.onclick = function() {
            console.log("Нажата кнопка Вперед!");
            setTabForCardTranzactions(cardId, page + 1, size);
        };
        
    }).fail(function() {
        alert("Ошибка загрузки транзакций!");
    });
}
