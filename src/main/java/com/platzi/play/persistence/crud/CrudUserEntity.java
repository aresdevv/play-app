package com.platzi.play.persistence.crud;

import com.platzi.play.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrudUserEntity extends JpaRepository<UserEntity, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario
     * @param username nombre de usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserEntity> findByUsername(String username);
    
    /**
     * Busca un usuario por su email
     * @param email email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserEntity> findByEmail(String email);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario dado
     * @param username nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el email dado
     * @param email email del usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca un usuario por nombre de usuario o email
     * @param username nombre de usuario
     * @param email email del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);
}
