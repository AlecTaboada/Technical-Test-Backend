package es.softtek.calificaciones.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import es.softtek.calificaciones.dto.Nota;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.softtek.calificaciones.dto.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

	@PostMapping("user")
	public ResponseEntity<User> login(@RequestParam("user") String username, @RequestParam("password") String pwd) {
		if(validaUsuario(username,pwd)){

			String token = getJWTToken(username);
			User user = new User();
			user.setUser(username);
			user.setToken(token);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

	private boolean validaUsuario(String username, String pwd) {
		if(username.equals("mercy") && pwd.equals("1234")){
			return true;

		}
		else if(username.equals("bertila") && pwd.equals("5130")){
			return true;
		}
		else {
			return false;
		}

	}


	private String getJWTToken(String userName) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities;
		if(userName.equals("bertila")){
			grantedAuthorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList("PROFESOR");
		}
		else{
			grantedAuthorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList("ALUMNO");
		}

		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(userName)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
}
