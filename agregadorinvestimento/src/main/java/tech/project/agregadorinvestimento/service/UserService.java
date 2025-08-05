package tech.project.agregadorinvestimento.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.project.agregadorinvestimento.controller.CreateUserDto;
import tech.project.agregadorinvestimento.controller.UpdateUserDto;
import tech.project.agregadorinvestimento.entity.User;
import tech.project.agregadorinvestimento.repository.UserRepository;

@Service
public class UserService {
    
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Example method to create a user
    public UUID createUser(CreateUserDto createUserDto) {

        // Logic to create a user using the UserRepository
        User user = new User();
        user.setUsername(createUserDto.username());
        user.setEmail(createUserDto.email());
        user.setPassword(createUserDto.password());
        
        // Save the user to the repository
        var userSaved = userRepository.save(user);

        return userSaved.getUserId();
    }

    // Example method to find a user by ID
    public Optional<User> getUserById(String userId) {

        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.findById(id);

        if (userExists.isPresent()) {
            var user = userExists.get();

            if(updateUserDto.username() != null) {
                user.setUsername(updateUserDto.username());
            }

            if(updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }
            
            userRepository.save(user);
        }
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = userRepository.existsById(id);

        if (userExists) {
            userRepository.deleteById(id);
        }
    }
}
