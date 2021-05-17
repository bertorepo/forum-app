package com.hubert.crudlogin;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.service.CustomerService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomerService customerService;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public SecurityConfig(
    BCryptPasswordEncoder passwordEncoder,
    CustomerService customerService
  ) {
    this.passwordEncoder = passwordEncoder;
    this.customerService = customerService;
  }

  //get Customer Details
  @Bean
  public UserDetailsService userDetailsService() {
    return (UserDetailsService) username -> {
      Optional<Customer> customer = customerService.findCustomerByUsername(
        username
      );
      if (customer.isEmpty()) {
        throw new UsernameNotFoundException("Username not fount " + username);
      }
      return customer.get();
    };
  }

  @Bean
  public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    StrictHttpFirewall firewall = new StrictHttpFirewall();
    firewall.setAllowUrlEncodedSlash(true);
    firewall.setAllowSemicolon(true);
    return firewall;
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);

    web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(userDetailsService())
      .passwordEncoder(passwordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .antMatchers("/admin/**")
      .hasAuthority("ADMIN")
      .antMatchers(
        "/",
        "/css/**",
        "/mdb/**",
        "/assets/**",
        "/js/**",
        "/img/**",
        "/webjars/**",
        "/register",
        "/summernote/**"
      )
      .permitAll()
      .anyRequest()
      .authenticated()
      .and()
      .formLogin()
      .loginPage("/login")
      .defaultSuccessUrl("/home")
      .permitAll()
      .and()
      .logout()
      .invalidateHttpSession(true)
      .clearAuthentication(true)
      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .logoutSuccessUrl("/")
      .deleteCookies()
      .permitAll()
      .and()
      .exceptionHandling()
      .accessDeniedPage("/403");
  }
}
