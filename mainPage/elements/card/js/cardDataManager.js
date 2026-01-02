// 1. Находим контейнер, куда будем вставлять карты
const cardList = document.querySelector('.card-list');

// 2. Находим сам шаблон
const template = document.querySelector('#card-template');

// Настройки пагинации
let currentPage = 1;      // Текущая страница
let itemsPerPage = 3;     // Сколько карточек выводить на одной странице

function renderCards(page) {
    const cardList = document.querySelector('.card-list');
    cardList.innerHTML = ''; // Очищаем список перед отрисовкой новой страницы

    // Рассчитываем индексы для цикла
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = Math.min(startIndex + itemsPerPage, cardsData.length);

    // Используем классический цикл for для пагинации
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

        // Добавляем готовую карточку в список
        cardList.appendChild(cardElement);
    }
}

// Вызов функции при загрузке (первая страница)
renderCards(currentPage);

//Пагинация карт
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
