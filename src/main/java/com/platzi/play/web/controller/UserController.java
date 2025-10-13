package com.platzi.play.web.controller;

import com.platzi.play.domain.dto.ReviewDto;
import com.platzi.play.domain.dto.UpdateUserDto;
import com.platzi.play.domain.dto.UserDto;
import com.platzi.play.domain.service.UserService;
import com.platzi.play.persistence.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/users")
@Tag(name = "Users", description = "Operaciones de gestión de perfil de usuario")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(
            summary = "Obtener perfil del usuario actual",
            description = "Retorna la información del usuario autenticado actualmente",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente")
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDto userDto = userService.getCurrentUser(user.getUsername());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/me")
    @Operation(
            summary = "Actualizar perfil del usuario",
            description = "Permite al usuario actualizar su información de perfil (nombre completo y email)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    @ApiResponse(responseCode = "409", description = "El email ya está en uso", content = @Content)
    public ResponseEntity<UserDto> updateProfile(
            @Valid @RequestBody UpdateUserDto updateUserDto,
            @AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDto updatedUser = userService.updateProfile(user.getUsername(), updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me/reviews")
    @Operation(
            summary = "Obtener reviews del usuario actual",
            description = "Retorna todas las reseñas creadas por el usuario autenticado",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Reviews obtenidas exitosamente")
    @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    public ResponseEntity<List<ReviewDto>> getCurrentUserReviews(@AuthenticationPrincipal UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<ReviewDto> reviews = userService.getUserReviews(user.getId());
        return ResponseEntity.ok(reviews);
    }
}
