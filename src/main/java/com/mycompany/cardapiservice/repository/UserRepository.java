package com.mycompany.cardapiservice.repository;

import com.mycompany.cardapiservice.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 */
public interface UserRepository extends JpaRepository<User, Long>{
    
    @Query("SELECT u from User u WHERE u.login = :userLogin")
    public Optional<User> findByLogin(@Param("userLogin") String userLogin);
    
    //в этом запросе проверяем - если параметр равен нулю или пользователю, то возвращаем результат
    //это позваляет выводить нам пользователей по фильтру фильтру если они есть,
    //и выводить всех пользоватеолей если фильтры пустые
    //(если в sql в Where возвращается везде false то запрос выведет пустоту)
    @Query("SELECT u FROM User u WHERE " +
       "(:login IS NULL OR u.login = :login) AND " +
       "(:surname IS NULL OR u.surname LIKE %:surname%) AND " +
       "(:name IS NULL OR u.name LIKE %:name%) AND " +
       "(:patronymic IS NULL OR u.patronymic LIKE %:patronymic%)")
    public Page<User> findAllByFilters(
        @Param("login") String login,
        @Param("surname") String surname,
        @Param("name") String name,
        @Param("patronymic") String patronymic,
        Pageable pageable
    );
}
