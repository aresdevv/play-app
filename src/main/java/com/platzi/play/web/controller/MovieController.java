    package com.platzi.play.web.controller;

    import com.platzi.play.domain.dto.MovieDto;
    import com.platzi.play.domain.dto.SuggestRequestDto;
    import com.platzi.play.domain.dto.UpdateMovieDto;
    import com.platzi.play.domain.service.MovieService;
    import com.platzi.play.domain.service.PlatziPlayAiService;
    import dev.langchain4j.service.UserMessage;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/movies")
    public class MovieController{

        private final MovieService movieService;
        private final PlatziPlayAiService  aiService;

        public MovieController(MovieService movieService,  PlatziPlayAiService aiService) {
            this.movieService = movieService;
            this.aiService = aiService;
        }

        @GetMapping
        public ResponseEntity<List<MovieDto>> getAll() {
            List<MovieDto> movies = this.movieService.getAll();
            if (movies == null || movies.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(movies);
        }

        @PostMapping("/suggest")
        public ResponseEntity<String> generateMoviesSuggestion(@RequestBody SuggestRequestDto suggestRequestDto) {
            return ResponseEntity.ok(this.aiService.generateMovieSuggestion(suggestRequestDto.userPreferences()));
        }

        @GetMapping("/{id}")
        public ResponseEntity<MovieDto> getById(@PathVariable Long id) {
            MovieDto movieDto = this.movieService.getById(id);
            if(movieDto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(movieDto);
        }

        @PostMapping
        public ResponseEntity<MovieDto> add(@RequestBody MovieDto movieDto){
            MovieDto movie = this.movieService.save(movieDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(movie);
        }

        @PutMapping("/{id}")
        public ResponseEntity<MovieDto> update(@PathVariable Long id, @RequestBody UpdateMovieDto movieDto){
            return ResponseEntity.ok(this.movieService.update(id,movieDto));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id){
            this.movieService.delete(id);
            return ResponseEntity.noContent().build();
        }


    }
