package cl.globallogic.ejerciciobci.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cl.globallogic.ejerciciobci.dto.SignUpRequestDto;
import cl.globallogic.ejerciciobci.dto.UserResponseDto;
import cl.globallogic.ejerciciobci.entity.Phone;
import cl.globallogic.ejerciciobci.entity.User;
import cl.globallogic.ejerciciobci.exception.AlreadyExistException;
import cl.globallogic.ejerciciobci.exception.ResourceNotFoundException;
import cl.globallogic.ejerciciobci.repository.IPhoneRepository;
import cl.globallogic.ejerciciobci.repository.IUserRepository;
import cl.globallogic.ejerciciobci.service.impl.PasswordServiceImpl;
import cl.globallogic.ejerciciobci.service.impl.UserServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {


	private final IUserRepository userRepository = Mockito.mock(IUserRepository.class);
	private final IPhoneRepository phoneRepository = Mockito.mock(IPhoneRepository.class);
	private final ModelMapper modelMapper = new ModelMapper();
	private final PasswordServiceImpl passwordServiceImpl = new PasswordServiceImpl();
	private final UserServicesImpl serviceUserImpl = new UserServicesImpl(userRepository, phoneRepository, passwordServiceImpl,modelMapper);
	private final UserController userController = new UserController(serviceUserImpl);

	@BeforeEach
	void setUp() {
		User validUser = createValidUser();
		Optional<User> userDb = Optional.of(validUser);
		Mockito.when(userRepository.findByIdUser(validUser.getIdUser())).thenReturn(userDb);
	}

	@Test
	void testCreateUser_OK() {
		User validUser = createValidUser();
		SignUpRequestDto userRequest = modelMapper.map(validUser, SignUpRequestDto.class);

		Mockito.when(userRepository.save(any(User.class))).thenReturn(validUser);

		ResponseEntity<UserResponseDto> response = userController.createUser(userRequest);
		assertEquals(response.getBody().getEmail() , "test@test.cl");
	}

	@Test
	void testCreateUser_Exist() {
		User user = createValidUser();
		try{
			Optional<User> userExist = Optional.of(user);
			SignUpRequestDto userRequest = modelMapper.map(user, SignUpRequestDto.class);
			Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(userExist);

			ResponseEntity<UserResponseDto> response = userController.createUser(userRequest);

		} catch (AlreadyExistException e) {
			assertThatExceptionOfType(AlreadyExistException.class)
					.isThrownBy(() -> { throw new AlreadyExistException("Usuario ya existe con email : " + user.getEmail()); })
					.withMessage("Usuario ya existe con email : " + user.getEmail());
		}
	}

	@Test
	void testCreateUser_NOK() {
		User user = createInvalidUser();
		try{
			Optional<User> userExist = Optional.of(user);
			SignUpRequestDto userRequest = modelMapper.map(user, SignUpRequestDto.class);
			Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(userExist);

			ResponseEntity<UserResponseDto> response = userController.createUser(userRequest);

		} catch (Exception e) {
			assertThatExceptionOfType(NullPointerException.class)
					.isThrownBy(() -> { throw new NullPointerException("Campos en request incompletos."); })
					.withMessage("Campos en request incompletos.");
		}
	}
	@Test
	void testGetUserById_OK() {

		ResponseEntity<UserResponseDto> response = userController.getUserById("6b6e49ae-8e84-11ec-b909-0242ac120002");
		assertEquals(Objects.requireNonNull(response.getBody()).getEmail() , "test@test.cl");
	}

	@Test
	void testGetUserById_NOK() {
		User user = createValidUser();
		try {

			Optional<User> userDb = Optional.empty();
			Mockito.when(userRepository.findByIdUser(user.getIdUser())).thenReturn(userDb);

			ResponseEntity<UserResponseDto> response = userController.getUserById("6b6e49ae-8e84-11ec-b909-0242ac120002");

		} catch (ResourceNotFoundException e) {
			assertThatExceptionOfType(ResourceNotFoundException.class)
					.isThrownBy(() -> { throw new ResourceNotFoundException("Usuario no encontrado. ID : " + user.getIdUser()); })
					.withMessage("Usuario no encontrado. ID : " + user.getIdUser());
		}
	}

	/**
	 * UUID generado en https://www.uuidgenerator.net/version1
	 * */
	//TODO TOKEN PARA PRUEBAS SIN DESARROLLAR
	private User createValidUser() {
		User user = new User();
		UUID uuid = UUID.fromString("6b6e49ae-8e84-11ec-b909-0242ac120002");
		user.setIdUser(uuid);
		user.setName("Test");
		user.setEmail("test@test.cl");
		user.setPassword("S1573M4s");
		user.setToken("Sin token");
		user.setCreated(LocalDateTime.now());
		user.setLastLogin(LocalDateTime.now());
		user.setActive(true);

		Phone phone = new Phone();
		phone.setCitycode(3);
		phone.setContrycode("5");
		phone.setNumber(1234556);
		phone.setUser(user);

		HashSet<Phone> phones = new HashSet<>();
		phones.add(phone);

		user.setPhones(phones);

		return user;
	}



	private User createInvalidUser() {
		User user = new User();
		user.setName("testInvalidUser");
		user.setEmail(null);
		user.setPassword(null);
		user.setActive(true);

		Phone phone = new Phone();
		phone.setCitycode(11);
		phone.setContrycode("5");
		phone.setNumber(1234556);
		phone.setUser(null);
		user.setPhones(null);

		return user;
	}

}
