package com.platzi.play.web.controller;

import com.platzi.play.domain.dto.CreateReviewDto;
import com.platzi.play.domain.dto.ReviewDto;
import com.platzi.play.domain.dto.UpdateReviewDto;
import com.platzi.play.domain.service.ReviewService;
import com.platzi.play.persistence.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Operaciones de calificaciones y reseñas de películas")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @Operation(
            summary = "Crear una nueva reseña",
            description = "Permite a un usuario autenticado crear una reseña para una película. Solo se permite una reseña por usuario por película.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Ya existe una reseña para esta película", content = @Content)
    public ResponseEntity<ReviewDto> createReview(
            @Valid @RequestBody CreateReviewDto createReviewDto,
            @AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ReviewDto review = reviewService.createReview(createReviewDto, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar una reseña",
            description = "Permite al usuario actualizar su propia reseña",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "403", description = "No tienes permiso para modificar esta reseña", content = @Content)
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    public ResponseEntity<ReviewDto> updateReview(
            @Parameter(description = "ID de la reseña a actualizar")
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewDto updateReviewDto,
            @AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ReviewDto review = reviewService.updateReview(id, updateReviewDto, user.getId());
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una reseña",
            description = "Permite al usuario eliminar su propia reseña",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "204", description = "Reseña eliminada exitosamente")
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "403", description = "No tienes permiso para eliminar esta reseña", content = @Content)
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID de la reseña a eliminar")
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reviewService.deleteReview(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener una reseña por ID",
            description = "Obtiene los detalles de una reseña específica"
    )
    @ApiResponse(responseCode = "200", description = "Reseña encontrada")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    public ResponseEntity<ReviewDto> getReviewById(
            @Parameter(description = "ID de la reseña")
            @PathVariable Long id) {
        ReviewDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/movie/{movieId}")
    @Operation(
            summary = "Obtener todas las reseñas de una película",
            description = "Retorna todas las reseñas asociadas a una película específica"
    )
    @ApiResponse(responseCode = "200", description = "Reseñas obtenidas exitosamente")
    @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content)
    public ResponseEntity<List<ReviewDto>> getReviewsByMovie(
            @Parameter(description = "ID de la película")
            @PathVariable Long movieId) {
        List<ReviewDto> reviews = reviewService.getReviewsByMovieId(movieId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Obtener todas las reseñas de un usuario",
            description = "Retorna todas las reseñas creadas por un usuario específico"
    )
    @ApiResponse(responseCode = "200", description = "Reseñas obtenidas exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(
            @Parameter(description = "ID del usuario")
            @PathVariable Long userId) {
        List<ReviewDto> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}/movie/{movieId}")
    @Operation(
            summary = "Obtener la reseña de un usuario para una película específica",
            description = "Retorna la reseña de un usuario para una película en particular"
    )
    @ApiResponse(responseCode = "200", description = "Reseña encontrada")
    @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    public ResponseEntity<ReviewDto> getUserReviewForMovie(
            @Parameter(description = "ID del usuario")
            @PathVariable Long userId,
            @Parameter(description = "ID de la película")
            @PathVariable Long movieId) {
        ReviewDto review = reviewService.getUserReviewForMovie(userId, movieId);
        return ResponseEntity.ok(review);
    }

    @GetMapping("/movie/{movieId}/average")
    @Operation(
            summary = "Obtener calificación promedio de una película",
            description = "Retorna el promedio de calificaciones de una película"
    )
    @ApiResponse(responseCode = "200", description = "Promedio calculado exitosamente")
    public ResponseEntity<Double> getMovieAverageRating(
            @Parameter(description = "ID de la película")
            @PathVariable Long movieId) {
        Double average = reviewService.getMovieAverageRating(movieId);
        return ResponseEntity.ok(average != null ? average : 0.0);
    }

    @GetMapping("/movie/{movieId}/count")
    @Operation(
            summary = "Obtener cantidad de reseñas de una película",
            description = "Retorna el número total de reseñas de una película"
    )
    @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    public ResponseEntity<Long> getMovieReviewCount(
            @Parameter(description = "ID de la película")
            @PathVariable Long movieId) {
        long count = reviewService.getMovieReviewCount(movieId);
        return ResponseEntity.ok(count);
    }
}

