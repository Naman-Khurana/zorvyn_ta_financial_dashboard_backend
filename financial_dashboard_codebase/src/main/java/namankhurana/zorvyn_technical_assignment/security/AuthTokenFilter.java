package namankhurana.zorvyn_technical_assignment.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    @Autowired
    public AuthTokenFilter(CustomUserDetailsService userDetailsService, JWTService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");


        try {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                if (authHeader == null) {
                    request.setAttribute("jwt_error", "JWT token is missing");
                }

                else if (!authHeader.startsWith("Bearer ")) {
                    request.setAttribute("jwt_error", "Invalid Authorization header");
                }

                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            final String userId = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userId != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserById(userId);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);


        } catch (ExpiredJwtException e) {
            request.setAttribute("jwt_error", "JWT token has expired");
            filterChain.doFilter(request, response);
        } catch (SignatureException e) {
            request.setAttribute("jwt_error", "Invalid JWT signature");
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            request.setAttribute("jwt_error", "Invalid JWT token");
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            request.setAttribute("jwt_error", "Authentication failed");
            filterChain.doFilter(request, response);
        }


    }
}
