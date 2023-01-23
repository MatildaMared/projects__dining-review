package com.matildamared.diningreview.user;

import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        validateUsername(username);
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable String username, @RequestBody User user) {
        Optional<User> optionalExistingUser = userRepository.findUserByUsername(username);

        if (optionalExistingUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        User existingUser = optionalExistingUser.get();

        if (!ObjectUtils.isEmpty(user.getAddress())) {
            existingUser.setAddress(user.getAddress());
        }

        if (!ObjectUtils.isEmpty(user.getCity())) {
            existingUser.setCity(user.getCity());
        }

        if (!ObjectUtils.isEmpty(user.getPostalCode())) {
            existingUser.setPostalCode(user.getPostalCode());
        }

        if (!ObjectUtils.isEmpty(user.getInterestedInEgg())) {
            existingUser.setInterestedInEgg(user.getInterestedInEgg());
        }

        if (!ObjectUtils.isEmpty(user.getInterestedInDairy())) {
            existingUser.setInterestedInDairy(user.getInterestedInDairy());
        }

        if (!ObjectUtils.isEmpty(user.getInterestedInPeanut())) {
            existingUser.setInterestedInPeanut(user.getInterestedInPeanut());
        }

        this.userRepository.save(existingUser);
    }

    private void validateUser(User user) {
        if (ObjectUtils.isEmpty(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be empty");
        }

        validateUsername(user.getUsername());
    }

    private void validateUsername(String username) {
        if (ObjectUtils.isEmpty(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be empty");
        }
    }
}
