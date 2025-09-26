package marketing.digital.rj.apiagendfy.infra.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private CustomUserDetailsService customUserDetailsService;
    private SecurityFilter securityFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, SecurityFilter securityFilter){
        this.customUserDetailsService = customUserDetailsService;
        this.securityFilter = securityFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.
                csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        //Rotas Publicas
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/cadastro").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/api-docs").permitAll()
                        .requestMatchers("/swagger-ui/swagger-config").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/swagger-config").permitAll()
                        .requestMatchers("/v3/api-docs").permitAll()

                        //Rotas admin
                        .requestMatchers(HttpMethod.GET,"/app/admin/**","/app/Admin/**").hasRole("ADMIN")

                        //Rotas Collaborator
                        .requestMatchers(HttpMethod.POST,"/app/collaborator/**", "/app/Collaborator/**").hasAnyRole("COLLABORATOR","ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/enterprise/**").hasAnyRole("USER","COLLABORATOR","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/enterprise/**").hasAnyRole("COLLABORATOR","ADMIN")


                        //Rotas de Usuarios
                        .requestMatchers(HttpMethod.GET, "/availability/**")
                        .hasAnyRole("USER","COLLABORATOR","ADMIN")


                        // Agendamentos: front faz POST /schedules
                        .requestMatchers(HttpMethod.POST, "/schedules/**")
                        .hasAnyAuthority("USER","COLLABORATOR","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/app/**").hasAnyAuthority("USER","COLLABORATOR","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/app/dashboard/**").hasAnyAuthority("USER","COLLABORATOR","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/dashboard/**").hasAnyAuthority("USER","COLLABORATOR","ADMIN")

                        .requestMatchers(HttpMethod.GET,  "/availability/**")
                        .hasAnyRole("USER","COLLABORATOR","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/appointments/**")
                        .hasAnyRole("USER","COLLABORATOR","ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/appointments/**")
                        .hasAnyRole("USER","COLLABORATOR","ADMIN")

                        //Demais rotas precisa está verificado
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of("http://localhost:3000", "https://seu-front.com")); //No caso o primeiro é o meu ambiente de trabalho e o segundo e o dominio que o site vai ser upado
        cors.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cors.setAllowedHeaders(List.of("Authorization","Content-Type"));
        cors.setAllowCredentials(true); // se usar cookies (httponly) ou credenciais
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
