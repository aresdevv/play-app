    package com.platzi.play.web.controller;

    import com.platzi.play.domain.dto.MovieDto;
    import com.platzi.play.domain.dto.SuggestRequestDto;
    import com.platzi.play.domain.dto.UpdateMovieDto;
    import com.platzi.play.domain.service.MovieService;
    import com.platzi.play.domain.service.PlatziPlayAiService;
    import dev.langchain4j.service.UserMessage;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.Parameter;
    import io.swagger.v3.oas.annotations.media.Content;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.tags.Tag;
    import jakarta.validation.Valid;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/movies")
    @Tag(name = "Movies", description = "Operations about movies")
    public class MovieController{

        private final MovieService movieService;
        private final PlatziPlayAiService  aiService;

        public MovieController(MovieService movieService,  PlatziPlayAiService aiService) {
            this.movieService = movieService;
            this.aiService = aiService;
        }

        @GetMapping
        @Operation(
                summary = "Obtener todas las películas",
                description = "Retorna una lista de todas las películas disponibles en el sistema",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Lista de películas obtenida exitosamente"),
                        @ApiResponse(responseCode = "404", description = "No se encontraron películas", content = @Content)
                }
        )
        public ResponseEntity<List<MovieDto>> getAll() {
            List<MovieDto> movies = this.movieService.getAll();
            if (movies == null || movies.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(movies);
        }

        @PostMapping("/suggest")
        @Operation(
                summary = "Generar sugerencias de películas con IA",
                description = "Utiliza inteligencia artificial para generar recomendaciones de películas basadas en las preferencias del usuario",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Sugerencias generadas exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
                }
        )
        public ResponseEntity<String> generateMoviesSuggestion(
                @Parameter(description = "Preferencias del usuario para generar sugerencias")
                @Valid @RequestBody SuggestRequestDto suggestRequestDto) {
            return ResponseEntity.ok(this.aiService.generateMovieSuggestion(suggestRequestDto.userPreferences()));
        }

        @GetMapping("/{id}")
        @Operation(
                summary = "Obtener una pelicula por su identificador",
                description = "Retorna la pelicula que coincida con el identificador enviado",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Pelicula encontrada"),
                        @ApiResponse(responseCode = "404", description = "Pelicula no encontrada", content = @Content)
                }
        )
        public ResponseEntity<MovieDto> getById(@Parameter(description = "Identificador de la pelicula a recuperar", example = "9") @PathVariable Long id) {
            MovieDto movieDto = this.movieService.getById(id);
            if(movieDto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(movieDto);
        }

        @PostMapping
        @Operation(
                summary = "Crear una nueva película",
                description = "Agrega una nueva película al sistema",
                responses = {
                        @ApiResponse(responseCode = "201", description = "Película creada exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "409", description = "La película ya existe", content = @Content)
                }
        )
        public ResponseEntity<MovieDto> add(
                @Parameter(description = "Datos de la película a crear")
                @Valid @RequestBody MovieDto movieDto){
            MovieDto movie = this.movieService.save(movieDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(movie);
        }

        @PutMapping("/{id}")
        @Operation(
                summary = "Actualizar una película existente",
                description = "Actualiza los datos de una película existente en el sistema",
                responses = {
                        @ApiResponse(responseCode = "200", description = "Película actualizada exitosamente"),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content)
                }
        )
        public ResponseEntity<MovieDto> update(
                @Parameter(description = "Identificador de la película a actualizar", example = "1")
                @PathVariable Long id, 
                @Parameter(description = "Datos actualizados de la película")
                @RequestBody @Valid UpdateMovieDto movieDto){
            return ResponseEntity.ok(this.movieService.update(id,movieDto));
        }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una película",
            description = "Elimina una película del sistema por su identificador",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Película eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Película no encontrada", content = @Content)
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador de la película a eliminar", example = "1")
            @PathVariable Long id){
        this.movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import-from-tmdb/{tmdbId}")
    @Operation(
            summary = "Importar película desde TMDB",
            description = "Importa una película desde The Movie Database a la base de datos local. " +
                    "Si la película ya existe, retorna la existente. " +
                    "Esto permite hacer reviews sobre películas de TMDB sin crearlas manualmente.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Película importada exitosamente"),
                    @ApiResponse(responseCode = "200", description = "Película ya existía, se retorna la existente"),
                    @ApiResponse(responseCode = "404", description = "Película no encontrada en TMDB", content = @Content),
                    @ApiResponse(responseCode = "400", description = "ID de TMDB inválido", content = @Content)
            }
    )
    public ResponseEntity<MovieDto> importFromTmdb(
            @Parameter(description = "ID de la película en TMDB", example = "603")
            @PathVariable Long tmdbId) {
        MovieDto importedMovie = this.movieService.importFromTmdb(tmdbId);
        return ResponseEntity.status(HttpStatus.CREATED).body(importedMovie);
    }


}
