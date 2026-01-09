//Скрипт с методами отрисовки данных карт пользователя на главной

// 1. Находим контейнер, куда будем вставлять карты
const cardList = document.querySelector('.card-list');

// 2. Находим сам шаблон
const template = document.querySelector('#card-template');

/**
 * Создание/Отрисовка данных карт после получения массивов с данными о картах пользователя.
 * (Вызываем ТОЛЬКО после получения данных о картах, в файле cardData.js - после получения данных карт:
 * Этот метод только отрисоввывает карты по полученному массиву карт)
 * @param page Номер страницы для отрисовки карт (пагинация)
 */
function renderCards(page) {
    const cardList = document.querySelector('.card-list');
    cardList.innerHTML = ''; // Очищаем список перед отрисовкой новой страницы

    // Рассчитываем индексы для цикла
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = Math.min(startIndex + itemsPerPage, cardsData.length);

    // Используем классический цикл for для пагинации 
    //(обходим каждую карту в цикле для установки данных)
    for (let i = startIndex; i < endIndex; i++) {
        const data = cardsData[i]; // Берем текущий элемент массива по индексу

        // Клонируем содержимое шаблона
        const cardElement = template.content.cloneNode(true);

        // Наполняем клон данными из data
        cardElement.querySelector('.card-title').textContent = data.number;
        
        // Склеиваем ФИО
        const fullName = `${data.userDto.name} ${data.userDto.surname} ${data.userDto.patronymic}`;
        cardElement.querySelector('.card-owner-name').textContent = fullName;
        
        // Баланс и валюта
        cardElement.querySelector('.card-salary').textContent = `${data.balance} ${data.currencyDto.sign}`;
        
        // Статус блокировки
        cardElement.querySelector('.card-is-blocked').textContent = data.isBlocked ? "Заблокирована" : "Активна";

        //задаем уникальный идентификатор на кнопку "Подробнее" к каждой карте
        let buttonInfo = cardElement.querySelector('.card-info');
        buttonInfo.id = data.id;
        //слушатель нажатия
        buttonInfo.addEventListener('click', function() {
            setCurrentCardId(buttonInfo.id);
            setTabForCardData(data); // тут data - это один конкретный обьект из за цикла!!!
        });
        
        // Добавляем готовую карточку в список
        cardList.appendChild(cardElement);
    }
}

//Пагинация карт - вне функции
btnBack = document.getElementById("pag-card-button-back");
btnForward = document.getElementById("pag-card-button-forward");

btnBack.addEventListener('click', function(event) {
        if(currentPage != 1) {
            currentPage--;

            renderCards(currentPage);
        }
});

btnForward.addEventListener('click', function(event) {
        if((cardsData.length / currentPage) > currentPage) {
            currentPage++;

            renderCards(currentPage);
        }
});

// обработчик кнопок управления вкладками вне функции - для того чтобы не вешался несколько раз, каждый раз когда вызывается функция
let buttonCardInfo = document.getElementById("tab1");
buttonCardInfo.addEventListener('click', function() {
    if(getCurrentCardId() != null) { // если карта выбранна (в скрипте tabManager есть id выбранной карты)
        setTabForCardData(cardsData.find(item => item.id == getCurrentCardId())); //то из массива карт выбираем тот элемент, который соответствует Id из скрипта tabManager
    }
});

// обработчик кнопок управления вкладками вне функции - для того чтобы не вешался несколько раз, каждый раз когда вызывается функция
let buttonTranzaction = document.getElementById("tab2");
buttonTranzaction.addEventListener('click', function() {
   setTabForCardTranzactions(getCurrentCardId(), 0, 10); // тут data - это один конкретный обьект из за цикла!!!
});
