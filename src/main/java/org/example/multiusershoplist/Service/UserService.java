package org.example.multiusershoplist.Service;

import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserMangeRepo repo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    UserService(UserMangeRepo repo) {
        this.repo = repo;
    }

    /**
     * Create new user
     * @param user <- new user
     */
    public void createUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);

    }

    /**
     * Get User with this nick
     * @param nick  finding user nick
     * @return Optional object with found user (if found)
     */
    public Optional<User>  getUser(String nick){
        return repo.findByNick(nick);
    }

    /**
     * Get Useer with this email address
     * @param email  finding user email address
     * @return  optional object of found User
     */
    public Optional<User>  getUserByEmail(String email){
        return repo.findByEmail(email);
    }

    /**
     * Get All Users which nick start with
     * @param nick  first letter/s in finding nick
     * @return  all found Users
     */
    public List<User> getUsersWithNickStart(String nick){
        return  repo.findByNickStartingWith(nick);
    }

    /**
     * Change password
     * @param nick <- nick User which change password
     * @param newPassword <- new password
     * @return false if newPassword is the same as old
     */
    @Transactional
    public boolean changePassword(String nick, String newPassword){
        String oldPassword = repo.getUserPasswordByNick(nick);
        if(oldPassword.equals(newPassword))
            return false;
        repo.updatePassword(nick, newPassword);
        return  true;
    }

    /**
     * Change email
     * @param nick <- nick User which change email
     * @param newEmail <- new email
     */
    @Transactional
    public void changeEmail(String nick, String newEmail){
        repo.updateEmail(nick, newEmail);
    }


    @Transactional
    public void update(User user){
        repo.save(user);
    }


    public void addOrder(User user, Order order){
        user.addOrder(order);
        repo.save(user);
    }






}
