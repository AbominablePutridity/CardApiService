// 1. Находим контейнер, куда будем вставлять карты
const cardList = document.querySelector('.card-list');

// 2. Находим сам шаблон
const template = document.querySelector('#card-template');

// 3. Функция для создания карточки
cardsData.forEach(data => {
    // Клонируем содержимое шаблона (true — глубокое клонирование)
    const cardElement = template.content.cloneNode(true);

    // Наполняем клон данными
    cardElement.querySelector('.card-title').textContent = data.number;
    cardElement.querySelector('.card-owner-name').textContent = data.userDto;
    cardElement.querySelector('.card-salary').textContent = data.balance;
    cardElement.querySelector('.card-is-blocked').textContent = data.isBlocked;
    
    // const img = cardElement.querySelector('.card-image');
    // img.src = data.image;
    // img.alt = data.title;

    // 4. Добавляем готовую карточку в список
    cardList.appendChild(cardElement);
});

