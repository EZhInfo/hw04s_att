package com.example.hw04s_att.config;

import com.example.hw04s_att.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfiguration {
//    private final AuthenticationProvider authenticationProvider;
//
//    public SecurityConfig(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }

@Configuration
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;
    
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        //        //ОТКЛЮЧЕНИЕ ШИФРОВАНИЯ ПАРОЛЕЙ - НЕЛЬЗЯ!!!
        //        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //конфигугируем работу Spring Security
        //        http.csrf().disable() //отключение защиты от межсайтовой подделки запросов - НЕЛЬЗЯ!!!
        http
                .authorizeHttpRequests() //все страница должны быть авторизироваными
                .requestMatchers("/admin/**", "/admin/admin", "/admin/orders","/admin/orders/search").hasRole("ADMIN")
                //                .anyRequest().authenticated() //для всех остальных страниц вызывается метод authenticated()
                .requestMatchers("/authentication", "/registration", "/error", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/product/**", "/product/info/{id}", "/product/search").permitAll() // страницы доступные для всех, permitAll() - для неавторизированых тоже, сюда же добавлять и статику 1:29:15 №18; доступ на файлы без дочерних
                .anyRequest().hasAnyRole("USER", "ADMIN")
                .and() //дальше настраивается аутентификация и соединяется с настройкой доступа
                .formLogin().loginPage("/authentication") // url-запрос на защищенные страницы, переопределение базовой страницы
                .loginProcessingUrl("/process_login") //адрес куда отправляются данные с формы /authentication - уже не надо сощдававть метод в контроллере, Spring Security ждет объект с формы, а затем сверяет логин и пароль с данными бд
                .defaultSuccessUrl("/person_account", true) // путь после успешной аутентификации, второй параметр - перенаправление в любом случае после успешной аутентификации
                .failureUrl("/authentication?error") // путь при проваленной аутентификации, в запрос будет объект error для проверки на наличия на форме и выводе ошибки
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");
        return http.build();
    }
    
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }
    
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }
    
}
