package com.mobilishop.api.service.impl;

import com.mobilishop.api.enums.AppUserRole;
import com.mobilishop.api.exception.ApiRequestException;
import com.mobilishop.api.model.Role;
import com.mobilishop.api.model.User;
import com.mobilishop.api.repository.RoleRepository;
import com.mobilishop.api.repository.UserRepository;
import com.mobilishop.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    //private final PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final static String USER_NOT_FOUND_MESSAGE = "User with username %s not found";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username));
        }

        Collection<SimpleGrantedAuthority>  authorities = new ArrayList<>(user.getRoles().size());
        user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User createUser(User user) {
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
        user.setEnabled(true);
        // Set default values for createdAt and updatedAt
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // TODO: Send confirmation token to user's email


        return userRepository.save(user);
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
        User user = userRepository.findById(id).orElseThrow(() -> new ApiRequestException("User with id " + id + " not found"));
        userRepository.deleteById(id);
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
