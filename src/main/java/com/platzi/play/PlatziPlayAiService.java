package com.platzi.play;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface PlatziPlayAiService {

    @UserMessage("""
            Genera un saludo de bienvenida en español para una aplicación de
            gestor de peliculas
            (esto va salir en pantalla asi que sea directo el saludo, 
            sin opciones, tú elige la mejor, no me digas *esta es una opcion*, solo has el saludo
            """)
    String generateGreeting();
}
