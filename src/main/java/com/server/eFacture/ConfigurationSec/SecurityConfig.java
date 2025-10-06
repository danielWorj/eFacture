package com.server.eFacture.ConfigurationSec;

import com.server.eFacture.Filter.JwtFilter;
import com.server.eFacture.Service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Noter que c'est une configuration
@EnableWebSecurity //C'est pour dire que notre application doit être protégé par Spring Security
                    //Du coup c'est obligatoire de ce connecter pour avoir accès au serveur
@RequiredArgsConstructor  //Pour faire de l'injection
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailsService;

    private final JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder(){
        //Permet de crypter notre mot de passe
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http , PasswordEncoder passwordEncoder) throws Exception {
        //Pour authentifier les utilisateurs c'est à dire en plus de rechercher son nom utilisateur , on doit aussi vérifier son mot de passe.
        AuthenticationManagerBuilder  authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder); //Récupérer l'utilisateur et vérifier que les mots de passe correspondent
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //Cette méthode définie le filtre par lequel notre application doit passer avant d'arriver à notre controller

        //Construction de notre http
        return http
                .csrf(AbstractHttpConfigurer::disable) //Attaque connus
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/*").permitAll() //Autorisé les deux requêtes citées plus haut
                                .anyRequest().permitAll()) //Pour les autres requêtes , les authentifier
                .addFilterBefore(new JwtFilter(customUserDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class) //Avec jwt
                .build();
    }

}
