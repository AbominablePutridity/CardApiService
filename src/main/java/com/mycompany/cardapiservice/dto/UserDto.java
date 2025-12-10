package com.mycompany.cardapiservice.dto;

import com.mycompany.cardapiservice.dto.interfaces.TransferableDtoToEntity;
import com.mycompany.cardapiservice.entity.Currency;
import com.mycompany.cardapiservice.entity.User;
import io.jsonwebtoken.security.Password;

/**
 *
 */
public class UserDto implements TransferableDtoToEntity<User> {
    private Long id;
    private String login;
    private String surname;
    private String name;
    private String patronymic;
    private String role;
    
    public UserDto(){} // для сериализатора
    
    public UserDto(User user)
    {
        id = user.getId();
        login = user.getLogin();
        surname = user.getSurname();
        name = user.getName();
        patronymic = user.getPatronymic();
        role = user.getRole();
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Перевод DTO класса в сущность
     * тут добавляем аттрибуты без ключей - ключи добавляем в сервисах.
     * (для унификации POST запроса, конкретная реализация перевода в сущность)
     * @return Обьект новой сущности (для сохранения в бд)
     */
    @Override
    public User toEntity() {
        User user = new User();
        
        return setDataInObject(user);
    }
    
    /**
     * Выношу логику в отдельный полноценный метод для того чтобы не было повторений кода
     * @param cardObject Обьект сущности, в который сохраняем данные из этого DTO класса
     * @return Обьект сущности с данными
     */
    private User setDataInObject(User user) {
        user.setName(name);
        user.setSurname(surname);
        user.setRole(role);
        user.setLogin(login);
        
        return user;
    }
    
    /**
     * Метод для обновления полей сущности из DTO класса
     * @param objectForUpdate Обьект сущности, которую нужно обновить
     * @param isSaveByPart
     * @return 
     */
    @Override
    public User toEntityWithFieldsCondition(User objectForUpdate, Boolean isSaveByPart) {
        if (isSaveByPart) {
            if(name != null)
            {
                objectForUpdate.setName(name);
            }

            if(surname != null)
            {
                objectForUpdate.setSurname(surname);
            }
            
            if(patronymic != null)
            {
                objectForUpdate.setPatronymic(patronymic);
            }
            
            if(role != null)
            {
                objectForUpdate.setRole(role);
            }
            
            if(login != null)
            {
                objectForUpdate.setLogin(login);
            }
        } else {
            objectForUpdate = setDataInObject(objectForUpdate);
        }
        
        return objectForUpdate;
    }
}
