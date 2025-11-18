package com.mycompany.cardapiservice.dto.auth;

/**
 * Класс для передачи в теле запроса на роуте логина в систему логина и пароля пользователя для выдачи токена
 */
public class AuthorizationDto {
    private String login;
    private String password;
    
    public AuthorizationDto(){} //для сериализатора

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
