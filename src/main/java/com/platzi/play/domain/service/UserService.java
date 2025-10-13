package com.platzi.play.domain.service;

import com.platzi.play.domain.dto.ReviewDto;
import com.platzi.play.domain.dto.UpdateUserDto;
import com.platzi.play.domain.dto.UserDto;
import com.platzi.play.domain.exception.UserAlreadyExistsException;
import com.platzi.play.domain.exception.UserNotFoundException;
import com.platzi.play.domain.repository.UserRepository;
import com.platzi.play.persistence.crud.CrudUserEntity;
import com.platzi.play.persistence.entity.UserEntity;
import com.platzi.play.persistence.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CrudUserEntity crudUserEntity;
    private final UserMapper userMapper;
    private final ReviewService reviewService;

    public UserService(UserRepository userRepository, 
                      CrudUserEntity crudUserEntity,
                      UserMapper userMapper,
                      ReviewService reviewService) {
        this.userRepository = userRepository;
        this.crudUserEntity = crudUserEntity;
        this.userMapper = userMapper;
        this.reviewService = reviewService;
    }

    public UserDto getCurrentUser(String username) {
        UserDto user = userRepository.getByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado: " + username);
        }
        return user;
    }

    public UserDto getUserById(Long userId) {
        UserDto user = userRepository.getById(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        return user;
    }

    @Transactional
    public UserDto updateProfile(String username, UpdateUserDto updateUserDto) {
        UserEntity userEntity = crudUserEntity.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + username));

        if (updateUserDto.email() != null && !updateUserDto.email().equals(userEntity.getEmail())) {
            if (userRepository.existsByEmail(updateUserDto.email())) {
                throw new UserAlreadyExistsException("El email ya está en uso");
            }
            userEntity.setEmail(updateUserDto.email());
        }

        if (updateUserDto.nombreCompleto() != null) {
            userEntity.setNombreCompleto(updateUserDto.nombreCompleto());
        }

        UserEntity savedEntity = crudUserEntity.save(userEntity);
        return userMapper.toDto(savedEntity);
    }

    public List<ReviewDto> getUserReviews(Long userId) {
        UserDto user = userRepository.getById(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
        }
        return reviewService.getReviewsByUserId(userId);
    }
}
