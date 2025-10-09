package com.platzi.play.persistence;

import com.platzi.play.domain.dto.UserDto;
import com.platzi.play.domain.repository.UserRepository;
import com.platzi.play.persistence.crud.CrudUserEntity;
import com.platzi.play.persistence.entity.UserEntity;
import com.platzi.play.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserEntityRepository implements UserRepository {

    private final CrudUserEntity crudUserEntity;
    private final UserMapper userMapper;

    public UserEntityRepository(CrudUserEntity crudUserEntity, UserMapper userMapper) {
        this.crudUserEntity = crudUserEntity;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getAll() {
        return userMapper.toDtoList(crudUserEntity.findAll());
    }

    @Override
    public UserDto getById(Long id) {
        UserEntity userEntity = crudUserEntity.findById(id).orElse(null);
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto getByUsername(String username) {
        UserEntity userEntity = crudUserEntity.findByUsername(username).orElse(null);
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto getByEmail(String email) {
        UserEntity userEntity = crudUserEntity.findByEmail(email).orElse(null);
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto getByUsernameOrEmail(String usernameOrEmail) {
        UserEntity userEntity = crudUserEntity.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElse(null);
        return userMapper.toDto(userEntity);
    }

    @Override
    public UserDto save(UserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        return userMapper.toDto(crudUserEntity.save(userEntity));
    }

    @Override
    public UserDto update(UserDto userDto) {
        UserEntity existingEntity = crudUserEntity.findById(userDto.id()).orElse(null);
        if (existingEntity == null) {
            return null;
        }
        
        // Actualizar campos
        existingEntity.setUsername(userDto.username());
        existingEntity.setEmail(userDto.email());
        existingEntity.setNombreCompleto(userDto.nombreCompleto());
        existingEntity.setRole(userDto.role());
        existingEntity.setActivo(userDto.activo());
        existingEntity.setEmailVerificado(userDto.emailVerificado());
        
        return userMapper.toDto(crudUserEntity.save(existingEntity));
    }

    @Override
    public void deleteById(Long id) {
        crudUserEntity.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return crudUserEntity.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return crudUserEntity.existsByEmail(email);
    }

    @Override
    public UserEntity getEntityByUsernameOrEmail(String usernameOrEmail) {
        return crudUserEntity.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElse(null);
    }
}
