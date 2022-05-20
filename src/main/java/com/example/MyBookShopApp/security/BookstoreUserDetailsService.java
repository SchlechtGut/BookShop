package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.aop.SearchSection;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public BookstoreUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User bookstoreUser = userRepository.findByEmail(name);

        if(bookstoreUser!=null){
            return new BookstoreUserDetails(bookstoreUser);
        }else{
            throw new UsernameNotFoundException("user not found doh!");
        }
    }



}
