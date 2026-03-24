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
                        .requestMatchers(HttpMethod.POST, "/usuarios/funcionarios").authenticated()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        .requestMatchers("/auth/me", "auth/status", "/dashboard").authenticated()

                        .requestMatchers(HttpMethod.GET, "/pedidos/cardapio/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/diponiveis").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/categoria/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/mais-vendidos").authenticated()

                        .requestMatchers(HttpMethod.GET, "/mesas").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/mesas/disponiveis").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/mesas/{id}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/mesas/numero/{numero}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.POST, "/mesas").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/mesas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/mesas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/mesas/{id}/status").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.POST, "/mesas/{id}/qrcode").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/mesas/qrcode/limpar-expirados").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/mesas/qrcodes/ativos").hasAnyRole("ADMINISTRADOR", "ATENDENTE")

                        .requestMatchers(HttpMethod.POST, "/mesas/qrcode/{id}/validar").authenticated()

                        .requestMatchers(HttpMethod.GET, "/mesas/iot").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET,"/mesas/iot/online").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET,"/mesas/{id}/iot").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/mesas/iot/{idConfig}/comunicar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/mesas/iot/{idConfig}/firmware").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/produtos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/codigo/{codigo}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/produtos").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/produtos/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/produtos/{id}/disponibilidade").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/produtos/{id}/aprovar-preco").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/produtos/{id}").hasRole("ADMINISTRADOR")

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
