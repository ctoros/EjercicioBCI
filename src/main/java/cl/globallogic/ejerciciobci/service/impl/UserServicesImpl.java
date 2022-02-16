package cl.globallogic.ejerciciobci.service.impl;

import cl.globallogic.ejerciciobci.dto.PhoneDto;
import cl.globallogic.ejerciciobci.dto.SignUpRequestDto;
import cl.globallogic.ejerciciobci.dto.UserResponseDto;
import cl.globallogic.ejerciciobci.entity.Phone;
import cl.globallogic.ejerciciobci.entity.User;
import cl.globallogic.ejerciciobci.exception.AlreadyExistException;
import cl.globallogic.ejerciciobci.exception.ResourceNotFoundException;
import cl.globallogic.ejerciciobci.repository.IPhoneRepository;
import cl.globallogic.ejerciciobci.repository.IUserRepository;

import cl.globallogic.ejerciciobci.service.IPasswordService;
import cl.globallogic.ejerciciobci.service.IUserServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyList;


@Service
@RequiredArgsConstructor
public class UserServicesImpl implements IUserServices, UserDetailsService{

	protected final IUserRepository userRepo;
	protected final IPhoneRepository phoneRepo;
	protected final IPasswordService passwordService;
	protected final ModelMapper modelMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public UserResponseDto createUser(SignUpRequestDto userReq) {
		UserResponseDto response = null;
		User user = modelMapper.map(userReq, User.class);
		user.setPassword(passwordService.hash(userReq.getPassword()));
		user.setToken(createToken(userReq.getEmail()));
		user.setCreated(LocalDateTime.now());
		user.setLastLogin(LocalDateTime.now());
		user.setActive(true);

		Optional<User> existe = this.userRepo.findByEmail(user.getEmail());
		if(existe.isPresent()){
			throw new AlreadyExistException("Ya existe : " + userReq.getEmail() + ". Intente con otro email");

		}

		User userDb = this.userRepo.save(user);
		Set<PhoneDto> phones = userReq.getPhones();
		userDb.getPhones().addAll((phones.stream().map(phone -> {

			Phone telefono = new Phone();
			telefono.setCitycode(phone.getCitycode());
			telefono.setContrycode(phone.getContrycode().toUpperCase().trim());
			telefono.setNumber(phone.getNumber());
			telefono.setUser(userDb);
			phoneRepo.save(telefono);
			return telefono;

		}).collect(Collectors.toList())));

		User userSaved = this.userRepo.save(userDb);
		return modelMapper.map(userSaved, UserResponseDto.class);
	}

	@Override
	public UserResponseDto getUserById(String id) {
		UUID uuid = UUID.fromString(id);

		Optional<User> userDb = this.userRepo.findByIdUser(uuid);
		if(userDb.isPresent()){
			return modelMapper.map(userDb.get(), UserResponseDto.class);
		}else {
			throw new ResourceNotFoundException("Usuario no encontrado con id : " + id);
		}
	}

	@Override
	public UserResponseDto findByEmail(String email) {
		User user = userRepo.findByEmail(email).orElse(new User());
		return modelMapper.map(user, UserResponseDto.class);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserResponseDto userResponseDto = findByEmail(email);
		User user = modelMapper.map(userResponseDto, User.class);
		UUID uuid = UUID.fromString(userResponseDto.getIdUser());
		user.setIdUser(uuid);
		if (user.getEmail() == null) {
			throw new UsernameNotFoundException(email);
		}
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), user.getPassword(), user.isActive(),true,true,true , emptyList());
	}

	@Override
	public Map<String, Object> getDetails(String email) {
		Map<String, Object> details = new HashMap<>();
		UserDetails userDetails = loadUserByUsername(email);
		if (userDetails != null) {
			UserResponseDto userResponseDTO = findByEmail(email);
			details.put("user", userResponseDTO);
			details.put("userDetails", userDetails);
		}
		return details;
	}

	@Override
	public UserResponseDto update(UserResponseDto userResponseDto) {
		User user = modelMapper.map(userResponseDto, User.class);
		UUID uuid = UUID.fromString(userResponseDto.getIdUser());
		user.setIdUser(uuid);
		return modelMapper.map(userRepo.save(user), UserResponseDto.class);
	}

	@Override
	public Optional<User> findByToken(String token) {
		return userRepo.findByToken(token);
	}

	private String createToken(String username) {
		Claims claims = Jwts.claims().setSubject(username);
		Date expiration = Date.from(LocalDateTime.now(UTC).plusMinutes(60).toInstant(UTC));
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, "secretKey")
				.compact();
	}
}
