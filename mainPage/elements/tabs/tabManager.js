// Скрипт, отрисоввывающий на вкладках информацию о картах

// Находим элемент
let div = document.getElementById('screens');
let currentCardId = null; //для взятия данных о конкретной карте по ее Id-коду

/**
 * Для установки какого либо действия над данными конкретной карты, задаем ее Id - для взятия по нему данных.
 * @param cardId Id-код карты.
 */
function setCurrentCardId(cardId) {
    console.log("CurrentCardId = ", cardId);
    currentCardId = cardId;
}

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
