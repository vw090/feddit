package com.vweinert.fedditbackend.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vweinert.fedditbackend.models.User;
import com.vweinert.fedditbackend.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;
  public UserDetailsServiceImpl(UserRepository userRepository){
    this.userRepository = userRepository;
  }
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }

}
