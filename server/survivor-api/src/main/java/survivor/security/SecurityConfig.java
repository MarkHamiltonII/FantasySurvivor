package survivor.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;



@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();

        http.authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/create_account").permitAll()
                .antMatchers("/api/refresh_token").authenticated()
                .antMatchers(HttpMethod.GET,"/api/users").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/users/*").hasAnyRole("USER")
                .antMatchers(HttpMethod.PUT,"/api/users/*").hasAnyRole("USER")
                .antMatchers(HttpMethod.DELETE,"/api/users/*").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/api/castaway/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/castaway/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/api/castaway/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/castaway/all").permitAll()
                .antMatchers(HttpMethod.GET,"/api/castaway/*").permitAll()
                .antMatchers(HttpMethod.GET,"/api/leagues/all").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/leagues/*").hasAnyRole("USER")
                .antMatchers(HttpMethod.POST,"/api/leagues/**").hasAnyRole("LEAGUE_OWNER")
                .antMatchers(HttpMethod.PUT, "/api/leagues/*").hasAnyRole("LEAGUE_OWNER")
                .antMatchers(HttpMethod.DELETE, "/api/leagues/**").hasAnyRole("LEAGUE_OWNER")
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(),converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


}
