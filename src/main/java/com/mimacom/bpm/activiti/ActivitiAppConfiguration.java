package com.mimacom.bpm.activiti;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;



@Configuration
@EnableWebMvc
public class ActivitiAppConfiguration  {
	
		private Logger logger = LoggerFactory.getLogger(ActivitiAppConfiguration.class);
		
		
	
		@Bean
	    public UserDetailsService myUserDetailsService() {

	        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

	        String[][] usersGroupsAndRoles = {
	                {"tecnico", "password", "ROLE_ACTIVITI_USER"},
	                {"tecnico2", "password", "ROLE_ACTIVITI_USER"},
	                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
	        };

	        for (String[] user : usersGroupsAndRoles) {
	            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
	            logger.info("> Nuevo usuarios registrado: " + user[0] + " con los ROLES[" + authoritiesStrings + "]");
	            inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),
	                    authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));
	        }


	        return inMemoryUserDetailsManager;

	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
	  
}
