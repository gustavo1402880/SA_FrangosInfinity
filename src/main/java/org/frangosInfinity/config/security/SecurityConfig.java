package org.frangosInfinity.config.security;

import io.swagger.v3.oas.models.PathItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Security;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/clientes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/funcionarios").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // ROTA DO CLIENTE
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/mesas/{id}/qrcode/validar").authenticated()
                        .requestMatchers("/cardapio/**").authenticated()
                        // ROTA DO ATENDENTE
                        .requestMatchers(HttpMethod.GET,"/mesas").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/mesas/disponiveis").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET,"/mesas/{id}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/mesas/numero/{numero}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.POST,"/pedidos/manual").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/mesas/{id}/status").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        // ROTA DO COZINHEIRO
                        .requestMatchers(HttpMethod.GET, "/pedidos/em-preparo").hasAnyRole("ADMINISTRADOR", "COZINHEIRO")
                        .requestMatchers(HttpMethod.POST, "/pedidos/{id}/pronto").hasAnyRole("ADMINISTRADOR", "COZINHEIRO")
                        // ROTA CAIXA
                        .requestMatchers(HttpMethod.GET, "/pagamentos/**").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pagamentos").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH,"/pagametnos/{id}/confirmar").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        // ROTA DO ADMINISTRADOR
                        .requestMatchers(HttpMethod.POST,"/mesas").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/mesas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/mesas/**").hasRole("ADMINISITRADOR")
                        .requestMatchers(HttpMethod.POST,"/mesas/{id}/qrcode").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST,"/mesas/qrcode/limpar-expirados").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST,"/mesas/{id}/iot").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/mesas/iot/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE,"/mesas/iot/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST,"/mesas/iot/{idConfig}/comunicar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/mesas/iot/{idConfig}/firmware").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/mesas/iot").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/mesas/iot/online").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/mesas/{id}/iot").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers("/relatorios/**").hasAnyRole("ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authmanager (HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
