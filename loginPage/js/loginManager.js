// HTML: <input id="login" ...> <input id="password" ...>

// событие на отправку формы
document.querySelector('form').addEventListener('submit', (e) => {
  e.preventDefault(); // отменяем стандартное поведение отправки формы
  
  const login = document.getElementById('loginField').value; // берем данные из логина
  const password = document.getElementById('passwordField').value; // берем данные пароля
  
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
    // localStorage.setItem('jwtToken', token);
    // window.location.href = 'cards.html';
  }).fail(function() {
    alert("Login failed!");
  });

  console.log("The tocken = " + responseResult);
});


