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

    @Mapping(source = "title", target = "title")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "genre", target = "genre", qualifiedByName = "stringToGenre")
    @Mapping(source = "releaseDate", target = "releaseDate")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "status", target = "available", qualifiedByName = "stateToBoolean")
    @Mapping(source = "tmdbId", target = "tmdbId")
    @Mapping(source = "posterUrl", target = "posterUrl")
    @Mapping(source = "backdropUrl", target = "backdropUrl")
    @Mapping(source = "overview", target = "overview")
    @Mapping(source = "originalTitle", target = "originalTitle")
    @Mapping(source = "voteAverage", target = "voteAverage")
    @Mapping(source = "voteCount", target = "voteCount")
    @Mapping(source = "popularity", target = "popularity")
    @Mapping(source = "originalLanguage", target = "originalLanguage")
    MovieDto toDto(MovieEntity entity);
    List<MovieDto> toDtoList(List<MovieEntity> entities);

    @InheritInverseConfiguration
    @Mapping(source="genre", target = "genre", qualifiedByName = "genreToString")
    @Mapping(source="available", target = "status", qualifiedByName = "booleanToState")
    MovieEntity toEntity(MovieDto dto);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "releaseDate", source = "releaseDate")
    @Mapping(target = "rating", source = "rating")
    void updateEntityFromDto(UpdateMovieDto updateMovieDto, @MappingTarget MovieEntity entity);
}