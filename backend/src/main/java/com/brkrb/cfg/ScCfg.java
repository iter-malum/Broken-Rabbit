package com.brkrb.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class ScCfg {
    
    @Bean("pE")
    public PasswordEncoder x1() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence r) {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] d = md.digest(r.toString().getBytes(StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    for (byte b : d) sb.append(String.format("%02x", b));
                    return sb.toString();
                } catch (Exception e) { return null; }
            }
            @Override
            public boolean matches(CharSequence r, String s) { 
                return encode(r).equalsIgnoreCase(s); 
            }
        };
    }

    @Bean
    public SecurityFilterChain f2(HttpSecurity h) throws Exception {
        h.csrf().disable()
         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         .and()
         .authorizeRequests()
         .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
         .anyRequest().authenticated();
         
        return h.build();
    }
}