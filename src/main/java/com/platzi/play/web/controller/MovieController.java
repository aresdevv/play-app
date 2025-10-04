    package com.platzi.play.web.controller;

    import com.platzi.play.domain.dto.MovieDto;
    import com.platzi.play.domain.service.MovieService;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.util.List;

    @RestController
    @RequestMapping("/movies")
    public class MovieController{

        private final MovieService movieService;

        public MovieController(MovieService movieService) {
            this.movieService = movieService;
        }

        @GetMapping
        public ResponseEntity<List<MovieDto>> getAll() {
            List<MovieDto> movies = this.movieService.getAll();
            if (movies == null || movies.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(movies);
        }

        @GetMapping("/{id}")
        public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
            MovieDto movieDto = this.movieService.getById(id);
            if(movieDto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(movieDto);
        }
    }
