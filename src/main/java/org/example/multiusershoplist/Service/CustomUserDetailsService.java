package org.example.multiusershoplist.Service;

import org.example.multiusershoplist.Model.User;
import org.example.multiusershoplist.Model.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {



    private final UserService service;

    public CustomUserDetailsService(UserService service) {
        this.service = service;
    }


    @Override
    public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {

        User user =  service.getUser(nick).orElseThrow((() -> new UsernameNotFoundException(nick)));

        return new UserPrincipal(user);
    }
}
