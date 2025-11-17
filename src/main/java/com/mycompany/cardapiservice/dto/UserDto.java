package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.entity.User;

/**
 *
 */
public class UserDto {
    private Long id;
    private String login;
    private String surname;
    private String name;
    private String patronymic;
    
    public UserDto(){} // для сериализатора
    
    public UserDto(User user)
    {
        id = user.getId();
        login = user.getLogin();
        surname = user.getSurname();
        name = user.getName();
        patronymic = user.getPatronymic();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
}
