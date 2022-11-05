package com.example.budget.features.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet jwtParams = JwtClaimsSet.builder().issuer("server").issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.HOURS)).subject(authentication.getName()).claim("scope", scope)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwtParams)).getTokenValue();
    }
}
