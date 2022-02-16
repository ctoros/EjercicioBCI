package cl.globallogic.ejerciciobci.configuration;

import cl.globallogic.ejerciciobci.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
	
	protected final TokenService tokenService;

	@Override
	  public void configure(HttpSecurity http) {
	    JwtTokenFilter customFilter = new JwtTokenFilter(tokenService);
	    http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
	  }
}
