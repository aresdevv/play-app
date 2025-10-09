package com.platzi.play.domain.repository;

import com.platzi.play.domain.dto.UserDto;
import com.platzi.play.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     */
    List<UserDto> getAll();
    
    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario
     * @return Usuario encontrado o null
     */
    UserDto getById(Long id);
    
    /**
     * Obtiene un usuario por su nombre de usuario
     * @param username nombre de usuario
     * @return Usuario encontrado o null
     */
    UserDto getByUsername(String username);
    
    /**
     * Obtiene un usuario por su email
     * @param email email del usuario
     * @return Usuario encontrado o null
     */
    UserDto getByEmail(String email);
    
    /**
     * Obtiene un usuario por nombre de usuario o email
     * @param usernameOrEmail nombre de usuario o email
     * @return Usuario encontrado o null
     */
    UserDto getByUsernameOrEmail(String usernameOrEmail);
    
    /**
     * Guarda un nuevo usuario
     * @param userDto datos del usuario
     * @return Usuario guardado
     */
    UserDto save(UserDto userDto);
    
    /**
     * Actualiza un usuario existente
     * @param userDto datos actualizados del usuario
     * @return Usuario actualizado
     */
    UserDto update(UserDto userDto);
    
    /**
     * Elimina un usuario por su ID
     * @param id ID del usuario
     */
    void deleteById(Long id);
    
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
     * Obtiene la entidad de usuario por nombre de usuario o email (para autenticación)
     * @param usernameOrEmail nombre de usuario o email
     * @return Entidad de usuario o null
     */
    UserEntity getEntityByUsernameOrEmail(String usernameOrEmail);
}
