package com.mycompany.cardapiservice.enums;

/**
 *
 * @author User
 */
public enum Role {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");
          
    //значение перечисления
    private final String value;
    
    /**
     * Конструктор со значением
     * @param value значение перечисления
     */
    Role(String value) {
        this.value = value;
    }
    
    /**
     * геттер перечисления.
     * @return значение перечисления
     */
    public String getValue() {
        return value;
    }
}
