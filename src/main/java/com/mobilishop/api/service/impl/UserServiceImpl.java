package com.mobilishop.api.service.impl;

import com.mobilishop.api.email.EmailSender;
import com.mobilishop.api.enums.AppUserRole;
import com.mobilishop.api.exception.ApiRequestException;
import com.mobilishop.api.model.Card;
import com.mobilishop.api.model.ConfirmationToken;
import com.mobilishop.api.model.Role;
import com.mobilishop.api.model.User;
import com.mobilishop.api.repository.RoleRepository;
import com.mobilishop.api.repository.UserRepository;
import com.mobilishop.api.service.ConfirmationTokenService;
import com.mobilishop.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    //private final PasswordEncoder passwordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final static String USER_NOT_FOUND_MESSAGE = "User with username %s not found";

    private final EmailSender emailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username));
        }

        /*
        if ( !user.isEnabled() ) {
            //throw new ApiRequestException("User is not enabled");
            throw new UsernameNotFoundException("User is not enabled");
        }
        */

        Collection<SimpleGrantedAuthority>  authorities = new ArrayList<>(user.getRoles().size());
        user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User getCurrentUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public String createUser(User user) {
        System.out.println("UserServiceImpl.createUser()");
        // Check email validity
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ApiRequestException("Email is required");
        }
        // Check email format validity
        if (!user.getEmail().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            throw new ApiRequestException("Email format is invalid");
        }
        // Check username validity
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new ApiRequestException("Username is required");
        }
        // Check password validity
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ApiRequestException("Password is required");
        }
        // Check if user with the same email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ApiRequestException("User with email " + user.getEmail() + " already exists");
        }
        // Check if user with the same username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ApiRequestException("User with username " + user.getUsername() + " already exists");
        }
        // BCrypt password encryption
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword( encodedPassword );
        // Set default role
        user.setRoles(Set.of(roleRepository.findByName(AppUserRole.ROLE_USER.name())));

        // Set default values for locked and enabled
        user.setLocked(false);
        user.setEnabled(false);
        // Set default values for createdAt and updatedAt
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User newUser = userRepository.save(user);

        // Send confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                newUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // TODO: SEND EMAIL
        emailSender.send(
                user.getEmail(),
                buildEmailConfirmationLink(token)
        );

        return token;

    }

    private String buildEmailConfirmationLink(String token) {
        return "http://localhost:8080/api/v1/register/confirm?token=" + token;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        // check id validity
        if (id == null) {
            throw new ApiRequestException("Id is required");
        }
        return userRepository.findById(id).orElseThrow(() -> new ApiRequestException("User with id " + id + " not found"));
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.
                getToken(token)
                .orElseThrow(() -> new ApiRequestException("Confirmation token not found"));

        if ( confirmationToken.getConfirmedAt() != null ) {
            throw new ApiRequestException("Confirmation token already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if ( expiredAt.isBefore(LocalDateTime.now()) ) {
            throw new ApiRequestException("Confirmation token expired");
        }

        confirmationTokenService.setConfirmedAt(token);


        // Enable user
        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return "User " + user.getUsername() + " successfully confirmed";
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public User updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            throw new ApiRequestException("User with id " + id + " not found");
        }
        // Check email validity
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            // Check email format validity
            if (!user.getEmail().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
                throw new ApiRequestException("Email format is invalid");
            }
            // Check if user with the same email already exists
            if (userRepository.findByEmail(user.getEmail()) != null && !user.getEmail().equals(userToUpdate.getEmail())) {
                throw new ApiRequestException("User with email " + user.getEmail() + " already exists");
            }
            userToUpdate.setEmail(user.getEmail());
        }
        // Check username validity
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            // Check if user with the same username already exists
            if (userRepository.findByUsername(user.getUsername()) != null && !user.getUsername().equals(userToUpdate.getUsername())) {
                throw new ApiRequestException("User with username " + user.getUsername() + " already exists");
            }
            userToUpdate.setUsername(user.getUsername());
        }
        // Check password validity
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            // BCrypt password encryption
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            userToUpdate.setPassword( encodedPassword );
        }
        // update other fields
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setBirthday(user.getBirthday());
        userToUpdate.setGender(user.getGender());
        userToUpdate.setUpdatedAt(LocalDateTime.now());
        userToUpdate.setAddress(user.getAddress());

        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(Long id) {
        System.out.println("UserServiceImpl.deleteUser()");
        User user = userRepository.findById(id).orElseThrow(() -> new ApiRequestException("User with id " + id + " not found"));

        // if authenticated user is admin we can delete any user
        Collection<SimpleGrantedAuthority>  authorities = new ArrayList<>(user.getRoles().size());
        getCurrentUser().getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        // delete if user as authorities role admin
        if (authorities.contains(new SimpleGrantedAuthority(AppUserRole.ROLE_ADMIN.name()))) {
            userRepository.delete(user);
        } else {
            // if authenticated user is not admin we can only delete user with id = authenticated user id
            if (getCurrentUser().getUsername().equals(user.getUsername())) {
                userRepository.delete(user);
            } else {
                throw new ApiRequestException("You are not allowed to delete this user");
            }
        }
    }

    private User checkUserAndRoleExists(String userEmail, String roleName) {
        // Check if user exists
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ApiRequestException("User not found");
        }
        // Check if role exists
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            throw new ApiRequestException("Role not found");
        }

        return user;
    }

    @Override
    public void addRoleToUser(String userEmail, String roleName) {
        // Check if user exists
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ApiRequestException("User not found");
        }
        // Check if role exists
        Role role = user.getRoles().stream().filter(r -> r.getName().equals(roleName)).findFirst().orElse(null);
        if (role == null) {
            throw new ApiRequestException("Role not found");
        }
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void removeRoleFromUser(String userEmail, String roleName) {
        // Check if user exists
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new ApiRequestException("User not found");
        }
        // Check if role exists
        Role role = user.getRoles().stream().filter(r -> r.getName().equals(roleName)).findFirst().orElse(null);
        if (role == null) {
            throw new ApiRequestException("Role not found");
        }
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Override
    public Card addCardToUser( Long userId, Card card ) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ApiRequestException("User with id " + userId + " not found")
        );
        // check card number validity
        if (card.getCardNumber() == null || card.getCardNumber().isEmpty()) {
            throw new ApiRequestException("Card number is invalid");
        }
        // check card number format validity
        if (!card.getCardNumber().matches("^[0-9]{16}$")) {
            throw new ApiRequestException("Card number format is invalid");
        }
        // check card expiration date validity
        if (card.getExpirationDate() == null || card.getExpirationDate().isEmpty()) {
            throw new ApiRequestException("Card expiration date is invalid");
        }
        // check card expiration date format validity
        if (!card.getExpirationDate().matches("^[0-9]{4}-[0-9]{2}$")) {
            throw new ApiRequestException("Card expiration date format is invalid");
        }
        // check card cvv validity
        if (card.getCvv() == null || card.getCvv().isEmpty()) {
            throw new ApiRequestException("Card cvv is invalid");
        }
        // check card cvv format validity
        if (!card.getCvv().matches("^[0-9]{3}$")) {
            throw new ApiRequestException("Card cvv format is invalid");
        }
        // check if card have cardHolderName
        if (card.getCardHolderName() == null || card.getCardHolderName().isEmpty()) {
            throw new ApiRequestException("Card holder name is invalid");
        }

        card.setUser(user);
        user.getCards().add(card);
        Card newCard = userRepository.save(user).getCards().stream().filter(c -> c.getCardNumber().equals(card.getCardNumber())).findFirst().orElse(null);
        return newCard;
    }

    public void init() {
        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        Role roleGuest = new Role();
        roleGuest.setName("ROLE_GUEST");

        // Create admin user
        User userAdmin = new User();
        userAdmin.setEmail("admin@gmail.com");
        userAdmin.setUsername("admin");
        userAdmin.setPassword("admin");
        // Crypt password
        userAdmin.setPassword( bCryptPasswordEncoder.encode(userAdmin.getPassword()) );
        userAdmin.setFirstName("AdminFirstName");
        userAdmin.setLastName("AdminLastName");
        Set<Role> adminRoles = Set.of(roleAdmin);
        userAdmin.setRoles(adminRoles);
        userRepository.save(userAdmin);

        // Create user
        User user = new User();
        user.setEmail("user@gmail.com");
        user.setUsername("user");
        user.setPassword("user");
        // Crypt password
        user.setPassword( bCryptPasswordEncoder.encode(user.getPassword()) );
        user.setFirstName("UserFirstName");
        user.setLastName("UserLastName");
        Set<Role> userRoles = Set.of(roleUser);
        user.setRoles(userRoles);
        userRepository.save(user);

    }

}
