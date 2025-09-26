package com.platzi.play.domain.service;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import org.springframework.beans.factory.annotation.Value;

@AiService
public interface PlatziPlayAiService {

    @UserMessage("""
            Genera un saludo de bienvenida en español para una aplicación de
            gestor de peliculas {{plataform}}
            (esto va salir en pantalla asi que sea directo el saludo, 
            sin opciones, tú elige la mejor, no me digas *esta es una opcion*, solo has el saludo
            """)
    String generateGreeting(String plataform );
}
