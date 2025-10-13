package com.platzi.play.persistence.mapper;

import com.platzi.play.domain.dto.ReviewDto;
import com.platzi.play.persistence.entity.ReviewEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "user.username", target = "username"),
            @Mapping(source = "movie.id", target = "movieId"),
            @Mapping(source = "movie.title", target = "movieTitle"),
            @Mapping(source = "rating", target = "rating"),
            @Mapping(source = "comment", target = "comment"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    ReviewDto toReviewDto(ReviewEntity reviewEntity);

    List<ReviewDto> toReviewDtos(List<ReviewEntity> reviewEntities);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "movie", ignore = true)
    })
    ReviewEntity toReviewEntity(ReviewDto reviewDto);
}

