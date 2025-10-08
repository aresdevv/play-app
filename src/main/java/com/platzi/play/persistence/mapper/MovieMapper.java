package com.platzi.play.persistence.mapper;

import com.platzi.play.domain.dto.MovieDto;
import com.platzi.play.domain.dto.UpdateMovieDto;
import com.platzi.play.persistence.entity.MovieEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GenreMapper.class, StateMapper.class})
public interface MovieMapper {

    @Mapping(source = "titulo", target = "title")
    @Mapping(source = "duracion", target = "duration")
    @Mapping(source = "genero", target = "genre", qualifiedByName = "stringToGenre"                 )
    @Mapping(source = "fechaEstreno", target = "releaseDate")
    @Mapping(source = "clasificacion", target = "rating")
    @Mapping(source = "estado", target = "available", qualifiedByName = "stateToBoolean")
    MovieDto toDto(MovieEntity entity);
    List<MovieDto> toDtoList(List<MovieEntity> entities);

    @InheritInverseConfiguration
    @Mapping(source="genre" ,target = "genero", qualifiedByName = "genreToString")
    @Mapping(source="available", target = "estado", qualifiedByName = "booleanToState")
    MovieEntity toEntity(MovieDto dto);

    @Mapping(target = "titulo",source = "title")
    @Mapping(target = "fechaEstreno", source = "releaseDate")
    @Mapping(target = "clasificacion", source = "rating")
    void updateEntityFromDto(UpdateMovieDto updateMovieDto,@MappingTarget MovieEntity entity);
}