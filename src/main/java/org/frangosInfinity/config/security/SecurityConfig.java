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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        .requestMatchers("/auth/me", "auth/status", "/dashboard").authenticated()

                        .requestMatchers("/dashboard", "/mesas", "/validar-qrcode", "/cardapio", "/acompanhar-pedido").authenticated()


                        .requestMatchers(HttpMethod.GET, "/api/pedidos/subpedidos/cliente").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/cardapio/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/produtos/diponiveis").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/produtos/categoria/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/produtos/mais-vendidos").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/mesas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/mesas/disponiveis").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/mesas/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/mesas/numero/{numero}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.POST, "/api/mesas").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/mesas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/mesas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/mesas/{id}/status").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.POST, "/api/mesas/{id}/qrcode").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/mesas/qrcode/limpar-expirados").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/mesas/qrcodes/ativos").hasAnyRole("ADMINISTRADOR", "ATENDENTE")

                        .requestMatchers(HttpMethod.POST, "/api/mesas/qrcode/{id}/validar").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/mesas/iot").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET,"/api/mesas/iot/online").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET,"/api/mesas/{id}/iot").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/mesas/iot/{idConfig}/comunicar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/mesas/iot/{idConfig}/firmware").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/produtos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/produtos/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/produtos/codigo/{codigo}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/produtos").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/produtos/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/produtos/{id}/disponibilidade").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/produtos/{id}/aprovar-preco").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/produtos/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/produtos/categorias").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/produtos/categorias/ativas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/produtos/categorias/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/produtos/categorias").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/produtos/categorias/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/produtos/categorias/{id}/ativar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/produtos/categorias/{id}/desativar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/produtos/categorias/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/pedidos/carrinho").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/carrinho/itens").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/pedidos/carrinho/itens/{index}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/carrinho/itens/{index}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/pedidos").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/numero/{numero}").authenticated()

                        .requestMatchers(HttpMethod.POST,"/api/pedidos/{pedidoId}/subpedidos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/subpedidos/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/{pedidoId}/subpedidos").authenticated()

                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/subpedidos/{id}/confirmar").hasAnyRole("ADMINISTRADOR", "ATENDENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/subpedidos/{id}/preparar").hasAnyRole("ADMINISTRADOR", "COZINHEIRO")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/subpedidos/{id}/pronto").hasAnyRole("ADMINISTRADOR", "COZINHEIRO")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/subpedidos/{id}/entregar").hasAnyRole("ADMINISTRADOR", "ATENDENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/subpedidos/{id}/cancelar").hasAnyRole("ADMINISTRADOR", "ATENDENTE")

                        .requestMatchers(HttpMethod.GET, "/api/pedidos").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/periodo").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/mesa/{mesaId}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/subpedidos/status/{status}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.DELETE, "/api/pedidos/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/api/pagamentos").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET,"/api/pagamentos").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/{id}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/subpedido/{subpedidoId}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/status/{status}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/api/pagamentos/{id}/confirmar").hasAnyRole("ADMINISTRADOR", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/api/pagamentos/{id}/cancelar").hasAnyRole("ADMINISTRADOR", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/api/pagamentos/{id}/reembolsar").hasAnyRole("ADMINISTRADOR", "CAIXA")
                        .requestMatchers(HttpMethod.DELETE, "/api/pagamentos/{id}").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.POST, "/api/pagamentos/pix").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/pix/{id}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/pix/pagamento/{pagamentoId}").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")
                        .requestMatchers(HttpMethod.PATCH, "/api/pagamentos/pix/{pagamentoId}/renovar").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.GET, "/api/pagamentos/comprovantes/**").hasAnyRole("ADMINISTRADOR", "ATENDENTE", "CAIXA")

                        .requestMatchers(HttpMethod.POST, "/api/contas/cliente/{clienteId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/contas/cliente/{clienteId}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pontos/acumular").hasAnyRole("ADMINISTRADOR", "ATENDIMENTO")
                        .requestMatchers(HttpMethod.POST, "/api/pontos/resgatar").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/regras/ativas").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/contas").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/contas/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/admin/regras").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/regras/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/admin/regras/{id}/ativar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/admin/regras/{id}/desativar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/regras/{id}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/admin/regras").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/admin/processar-expiracao").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/notificacoes/{destinatario}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/notificacoes/{destinatario}/nao-lidas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/notificacoes/{destinatario}/count").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/notificacoes/{destinatario}/lida").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/notificacoes/{destinatario}/lidas").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/notificacoes/cozinheiro/**").hasAnyRole("ADMINISTRADOR", "COZINHEIRO")
                        .requestMatchers(HttpMethod.POST, "/api/notificacoes/atendente/**").hasAnyRole("ADMINISTRADOR", "ATENDENTE")
                        .requestMatchers(HttpMethod.POST, "/api/notificacoes/admin/**").hasRole("ADMINISTRADOR")

                        .requestMatchers("/relatorios/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasAnyRole("ADMINISTRADOR", "ATENDENTE")
                        .requestMatchers(HttpMethod.GET, "/usuarios/email/{email}").hasAnyRole("ADMINISTRADOR", "ATENDENTE")
                        .requestMatchers(HttpMethod.GET, "/usuarios/ativos").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/inativos").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/tipo/{tipo}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/usuarios/nivel-acesso/{nivel}").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/usuarios/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/email").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/senha").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/{id}/desativar").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/{id}/ativar").hasRole("ADMINISTRADOR")

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
