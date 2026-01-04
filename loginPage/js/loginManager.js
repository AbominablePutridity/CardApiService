// событие на отправку формы
document.querySelector('form').addEventListener('submit', (e) => {
  e.preventDefault(); // отменяем стандартное поведение отправки формы
  
  const login = document.getElementById('loginField').value; // берем данные из логина
  const password = document.getElementById('passwordField').value; // берем данные пароля

  resultLabel = document.getElementById("result");
  
  console.log(login, password);


  dataPost(
    '',
    'api/users/guest/getEntryTocken',
    {
      login: login, //"ivanov",
      password: password //"password123"
    }
  ).done(function(token) {
    // ТОКЕН ЗДЕСЬ!
    console.log("The token = " + token);
    
    // ВРУЧНУЮ сохраняем токен
    if(token != null && token != '') {
      sessionStorage.setItem('jwtToken', token);
      console.log("Токен сохранен:", token);
      window.location.href = '../mainPage/index.html';
    } else {
      resultLabel.textContent = "Ошибка: введены неверные данные!";
    }
    
  }).fail(function() {
    alert("Login failed!");
  });
});


