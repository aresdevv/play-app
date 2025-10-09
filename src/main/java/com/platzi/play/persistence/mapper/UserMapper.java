package com.platzi.play.persistence.mapper;

import com.platzi.play.domain.dto.UserDto;
import com.platzi.play.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "nombreCompleto", target = "nombreCompleto")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "ultimoAcceso", target = "ultimoAcceso")
    @Mapping(source = "activo", target = "activo")
    @Mapping(source = "emailVerificado", target = "emailVerificado")
    UserDto toDto(UserEntity entity);

    List<UserDto> toDtoList(List<UserEntity> entities);

    @Mapping(target = "password", ignore = true) // No mapear la contraseña por seguridad
    @Mapping(target = "authorities", ignore = true) // Ignorar authorities de UserDetails
    UserEntity toEntity(UserDto dto);
}
