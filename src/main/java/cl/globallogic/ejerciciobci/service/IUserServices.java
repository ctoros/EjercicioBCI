package cl.globallogic.ejerciciobci.service;



import cl.globallogic.ejerciciobci.dto.SignUpRequestDto;
import cl.globallogic.ejerciciobci.dto.UserResponseDto;
import cl.globallogic.ejerciciobci.entity.User;

import java.util.Map;
import java.util.Optional;

public interface IUserServices {

	UserResponseDto createUser(SignUpRequestDto request);
	
	UserResponseDto getUserById(String id);
	
	UserResponseDto findByEmail(String email);
	
	Map<String, Object> getDetails(String email);
	
	UserResponseDto update(UserResponseDto request);
	
	Optional<User> findByToken(String token);

}
