package tech.project.agregadorinvestimento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tech.project.agregadorinvestimento.controller.CreateUserDto;
import tech.project.agregadorinvestimento.controller.UpdateUserDto;
import tech.project.agregadorinvestimento.entity.User;
import tech.project.agregadorinvestimento.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // Assuming you have a UserService instance and a UserRepository mock set up
    // private UserService userService;
    // private UserRepository userRepository;
    @Mock
    private UserRepository userRepository;

    // Initialize the UserService with the mocked UserRepository
    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("should create a user")
        void shouldCreateAUser() {

            // Arrange
            var user = new User(
                        UUID.randomUUID(), 
                        "username", 
                        "email@example.com", 
                        "password123", 
                        Instant.now(), 
                        null);

            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            var input = new CreateUserDto(
                        "username",
                        "email@example.com",
                        "password123");

            // Act
            var output = userService.createUser(input);

            // Assert
            assertNotNull(output);

            var capturedUser = userArgumentCaptor.getValue();

            assertNotNull(capturedUser);
            assertEquals(input.email(), capturedUser.getEmail());
            assertEquals(input.password(), capturedUser.getPassword());
            assertEquals(input.username(), capturedUser.getUsername());
        }

        @Test
        @DisplayName("should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {

            // Arrange
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto(
                        "username",
                        "email@example.com",
                        "password123");


            // Act & Assert
            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }

    @Nested
    class getUserById {

        @Test
        @DisplayName("should get a user by id with success when optional is present")
        void shouldGetAUserByIdWithSuccessWhenOptionalIsPresent() {
            // Arrange
            var user = new User(
                        UUID.randomUUID(), 
                        "username", 
                        "email@example.com", 
                        "password123", 
                        Instant.now(), 
                        null);

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(user.getUserId().toString());

            // Assert
            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("should get a user by id with success when optional is empty")
        void shouldGetAUserByIdWithSuccessWhenOptionalIsEmpty() {
            // Arrange
            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            // Act
            var output = userService.getUserById(userId.toString());

            // Assert
            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class listUsers {

        @Test
        @DisplayName("should return all users with sucess")
        void shouldReturnAllUsersWithSucess() {

            // Arrange
            var user = new User(
                        UUID.randomUUID(), 
                        "username", 
                        "email@example.com", 
                        "password123", 
                        Instant.now(), 
                        null);
                
            var userList = List.of(user);

            doReturn(userList)
                    .when(userRepository)
                    .findAll();

            // Act
            var output = userService.listUsers();

            // Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());   
        }

    }

    @Nested
    class deleteById {

        @Test
        @DisplayName("should delete a user by id with success when user exists")
        void shouldDeleteAUserByIdWithSuccessWhenUserExists() {
            // Arrange
            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(userRepository)
                    .deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("should not delete a user by id with success when user does not exist")
        void shouldNotDeleteAUserByIdWithSuccessWhenUserDoesNotExist() {
            // Arrange
            doReturn(false)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();

            // Act
            userService.deleteById(userId.toString());

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1))
                        .existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUserById {

        @Test
        @DisplayName("should update a user by id with success when user exists")
        void shouldUpdateAUserByIdWithSuccessWhenUserExists() {
            // Arrange
            var updateUserDto = new UpdateUserDto(
                        "newUsername",
                        "newPassword"
                    );

            var user = new User(
                        UUID.randomUUID(), 
                        "username", 
                        "email@example.com", 
                        "password123", 
                        Instant.now(), 
                        null);

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            // Act
            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            // Assert
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
            assertEquals(user.getUserId(), userArgumentCaptor.getValue().getUserId());

            var userCaptured = userArgumentCaptor.getValue();
            
            assertEquals(updateUserDto.username(), userCaptured.getUsername());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).save(user);
            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("should not update a user by id with success when user does not exist")
        void shouldNotUpdateAUserByIdWithSuccessWhenUserDoesNotExist() {
            // Arrange
            var updateUserDto = new UpdateUserDto(
                        "newUsername",
                        "newPassword"
                    );

            var userId = UUID.randomUUID();

            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            // Act
            userService.updateUserById(userId.toString(), updateUserDto);

            // Assert
            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(0)).save(any());
            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
        }
    }
}