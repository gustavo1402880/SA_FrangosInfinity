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

                        .requestMatchers(HttpMethod.GET, "/produtos/categorias").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/categorias/ativas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/produtos/categorias/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/produtos/categorias").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/produtos/categorias/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/produtos/categorias/{id}/ativar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/produtos/categorias/{id}/desativar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/produtos/categorias/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/pedidos/carrinho").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pedidos/carrinho/itens").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/pedidos/carrinho/itens/{index}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/carrinho/itens/{index}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/pedidos").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pedidos/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pedidos/numero/{numero}").authenticated()

                        .requestMatchers(HttpMethod.POST,"/pedidos/{pedidoId}/subpedidos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/pedidos/subpedidos/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/pedidos/{pedidoId}/subpedidos").authenticated()

                        .requestMatchers(HttpMethod.PATCH, "/pedidos/subpedidos/{id}/confirmar").hasAnyRole("ADMINISTRADOR", "ATENDENTE")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/subpedidos/{id}/preparar").hasAnyRole("ADMINISTRADOR", "COZINHEIRO")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/subpedidos/{id}/pronto").hasAnyRole("ADMINISTRADOR", "COZINHEIRO")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/subpedidos/{id}/entregar").hasAnyRole("ADMINISTRADOR", "ATENDENTE")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/subpedidos/{id}/cancelar").hasAnyRole("ADMINISTRADOR", "ATENDENTE")

                        .requestMatchers(HttpMethod.GET, "/pedidos").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pedidos/periodo").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pedidos/mesa/{mesaId}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pedidos/subpedidos/status/{status}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.DELETE, "/pedidos/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/pagamentos").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET,"/pagamentos").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pagamentos/{id}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pagamentos/subpedido/{subpedidoId}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pagamentos/status/{status}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/pagamentos/{id}/confirmar").hasAnyRole("ADMINISTRADOR", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/pagamentos/{id}/cancelar").hasAnyRole("ADMINISTRADOR", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/pagamentos/{id}/reembolsar").hasAnyRole("ADMINISTRADOR", "CAIXA")
                        .requestMatchers(HttpMethod.DELETE, "/pagamentos/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/pagamentos/pix").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pagamentos/pix/{id}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/pagamentos/pix/pagamento/{pagamentoId}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/pagamentos/pix/{pagamentoId}/renovar").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.GET, "/pagamentos/comprovantes/**").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.POST, "/contas/cliente/{clienteId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/contas/cliente/{clienteId}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pontos/acumular").hasAnyRole("ADMINISTRADOR", "ATENDIMENTO")
                        .requestMatchers(HttpMethod.POST, "/pontos/resgatar").authenticated()
                        .requestMatchers(HttpMethod.GET, "/regras/ativas").authenticated()

                        .requestMatchers(HttpMethod.GET, "/contas").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/contas/{id}").hasRole("ADMINISTRADOR")

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
    AuthenticationManager authManager (HttpSecurity http) throws Exception
    {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
