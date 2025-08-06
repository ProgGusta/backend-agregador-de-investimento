package tech.project.agregadorinvestimento.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tech.project.agregadorinvestimento.controller.dto.AccountResponseDto;
import tech.project.agregadorinvestimento.controller.dto.CreateAccountDto;
import tech.project.agregadorinvestimento.controller.dto.CreateUserDto;
import tech.project.agregadorinvestimento.controller.dto.UpdateUserDto;
import tech.project.agregadorinvestimento.entity.Account;
import tech.project.agregadorinvestimento.entity.BillingAddress;
import tech.project.agregadorinvestimento.entity.User;
import tech.project.agregadorinvestimento.repository.AccountRepository;
import tech.project.agregadorinvestimento.repository.BillingAddressRepository;
import tech.project.agregadorinvestimento.repository.UserRepository;

@Service
public class UserService {
    
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
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

    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Logic to create an account for the user
        var account = new Account(
            null, // accountId will be generated automatically
            user,
            null, // billingAddress can be set later
            createAccountDto.description(),
            new ArrayList<>()
        );

        // Save the account to the repository
        accountRepository.save(account);

        var billingAddress = new BillingAddress(
            null,
            account,
            createAccountDto.street(),
            createAccountDto.number()
        );

        billingAddressRepository.save(billingAddress);
    }

    public List<AccountResponseDto> listAccounts(String userId) {

        var user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts()
                .stream()
                .map(ac -> new AccountResponseDto(ac.getAccountId().toString(), ac.getDescription()))
                .toList();
    }
}
