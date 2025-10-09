package com.platzi.play.domain.service;

import com.platzi.play.domain.dto.AuthResponseDto;
import com.platzi.play.domain.dto.LoginDto;
import com.platzi.play.domain.dto.RegisterDto;
import com.platzi.play.domain.dto.UserDto;
import com.platzi.play.domain.exception.UserAlreadyExistsException;
import com.platzi.play.domain.exception.UserNotFoundException;
import com.platzi.play.domain.repository.UserRepository;
import com.platzi.play.persistence.crud.CrudUserEntity;
import com.platzi.play.persistence.entity.UserEntity;
import com.platzi.play.persistence.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CrudUserEntity crudUserEntity;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository, 
                      CrudUserEntity crudUserEntity,
                      PasswordEncoder passwordEncoder, 
                      JwtService jwtService,
                      AuthenticationManager authenticationManager,
                      UserMapper userMapper) {
        this.userRepository = userRepository;
        this.crudUserEntity = crudUserEntity;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    /**
     * Registra un nuevo usuario
     * @param registerDto datos de registro
     * @return respuesta de autenticación con token
     */
    public AuthResponseDto register(RegisterDto registerDto) {
        // Validar que las contraseñas coincidan
        if (!registerDto.password().equals(registerDto.confirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(registerDto.username())) {
            throw new UserAlreadyExistsException("El nombre de usuario ya está en uso");
        }

        if (userRepository.existsByEmail(registerDto.email())) {
            throw new UserAlreadyExistsException("El email ya está registrado");
        }

        // Crear nuevo usuario
        UserEntity newUser = new UserEntity();
        newUser.setUsername(registerDto.username());
        newUser.setEmail(registerDto.email());
        newUser.setPassword(passwordEncoder.encode(registerDto.password()));
        newUser.setNombreCompleto(registerDto.nombreCompleto());
        newUser.setRole(UserEntity.UserRole.USER);
        newUser.setActivo(true);
        newUser.setEmailVerificado(true); // Por simplicidad, asumimos que el email está verificado

        // Guardar usuario directamente como entidad
        UserEntity savedEntity = crudUserEntity.save(newUser);
        UserDto savedUser = userMapper.toDto(savedEntity);

        // Generar token JWT
        String token = jwtService.generateToken(newUser);

        // Actualizar último acceso
        newUser.setUltimoAcceso(LocalDateTime.now());
        userRepository.update(userMapper.toDto(newUser));

        return createAuthResponse(token, savedUser);
    }

    /**
     * Autentica un usuario existente
     * @param loginDto datos de login
     * @return respuesta de autenticación con token
     */
    public AuthResponseDto login(LoginDto loginDto) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDto.usernameOrEmail(),
                    loginDto.password()
                )
            );

            // Obtener usuario autenticado
            UserEntity user = (UserEntity) authentication.getPrincipal();
            UserDto userDto = userRepository.getById(user.getId());

            if (userDto == null) {
                throw new UserNotFoundException("Usuario no encontrado");
            }

            // Generar token JWT
            String token = jwtService.generateToken(user);

            // Actualizar último acceso
            user.setUltimoAcceso(LocalDateTime.now());
            userRepository.update(userMapper.toDto(user));

            return createAuthResponse(token, userDto);

        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
    }

    /**
     * Crea la respuesta de autenticación
     * @param token token JWT
     * @param userDto datos del usuario
     * @return respuesta de autenticación
     */
    private AuthResponseDto createAuthResponse(String token, UserDto userDto) {
        AuthResponseDto.UserInfoDto userInfo = new AuthResponseDto.UserInfoDto(
            userDto.id(),
            userDto.username(),
            userDto.email(),
            userDto.nombreCompleto(),
            userDto.role(),
            userDto.ultimoAcceso()
        );

        return new AuthResponseDto(
            token,
            "Bearer",
            jwtService.getExpirationTimeInSeconds(),
            userInfo
        );
    }

}
