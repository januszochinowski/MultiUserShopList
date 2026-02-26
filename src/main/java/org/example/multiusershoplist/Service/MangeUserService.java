package org.example.multiusershoplist.Service;

import jakarta.persistence.EntityNotFoundException;
import org.example.multiusershoplist.Model.Order;
import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Repo.UserMangeRepo;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MangeUserService {

    private UserMangeRepo repo;

    MangeUserService(UserMangeRepo repo) {
        this.repo = repo;
    }

    /**
     * Create new user
     * @param user <- new user
     */
    public void createUser(User user){
        repo.save(user);
    }

    /**
     * Get User with this nick
     * @param nick <- finding user nick
     * @return Optional object with found user (if found)
     */
    public Optional<User>  getUser(String nick){
        return repo.findByNick(nick);
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










}
