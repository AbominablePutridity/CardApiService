// скрипт, для отрисовки получченных данных из запроса о текущем пользователе на главную страницу

/**
 * Отрисоввывает ФИО пользователя на главной
 */
function setUserData()
{
  userPanel = document.getElementById("username");

  userPanel.textContent = userData.surname + " " + userData.name + " " + userData.patronymic;
}

