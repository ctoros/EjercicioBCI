package cl.globallogic.ejerciciobci.service;

import cl.globallogic.ejerciciobci.dto.UserResponseDto;
import cl.globallogic.ejerciciobci.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

	protected final IUserServices userService;
	
	public String resolveToken(HttpServletRequest req) {
        String token = null;
        final String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null) {
            token = bearerToken;
        }
        return token;
    }
	
	public Authentication getAuthentication(String token) {
        Authentication authentication = null;
        token = token.replace("Bearer ", "");
        Map<String, Object> details = this.userService.getDetails(getUsername(token));
        System.out.println(details.toString());
        if (!details.isEmpty() && details.get("userDetails") != null) {
            UserDetails userDetails = (UserDetails) details.get("userDetails");
            authentication = new UsernamePasswordAuthenticationToken(userDetails, "", new ArrayList<>());
        }
        if (authentication != null && details.get("user") != null) {
            UserResponseDto userDTO = (UserResponseDto) details.get("user");
            userDTO.setLastLogin(LocalDateTime.now());
            userService.update(userDTO);
        }
        return authentication;
    }
	
	private String getUsername(String token) {
        return Jwts.parser().setSigningKey("secretKey").parseClaimsJws(token).getBody().getSubject();
    }
	
	public boolean validateToken(String token) {
        boolean validateToken = true;
        token = token.replace("Bearer ", "");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey("secretKey").parseClaimsJws(token);
            Optional<User> userOptional = userService.findByToken(token);
            if (userOptional.isPresent()) {
                if (userOptional.get().getToken().equals(token)
                        && claims.getBody().getExpiration().before(new Date())) {
                    validateToken = false;
                }
            } else {
                validateToken = false;
            }

        } catch (JwtException | IllegalArgumentException | ClassCastException e) {
            validateToken = false;
        }
        return validateToken;
    }
}
