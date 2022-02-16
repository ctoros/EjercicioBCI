package cl.globallogic.ejerciciobci.controller;

import cl.globallogic.ejerciciobci.dto.SignUpRequestDto;
import cl.globallogic.ejerciciobci.dto.UserResponseDto;
import cl.globallogic.ejerciciobci.service.IUserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class UserController {

	protected final IUserServices userService;

	/*
	* endpoint para la creacion del usuario.
	* Valida que exista el usuario antes de crearlo mediante el email
	* */
	@PostMapping("/sign-up")
	public ResponseEntity<UserResponseDto> createUser( @RequestBody @Valid SignUpRequestDto request){
		return ResponseEntity.ok().body(this.userService.createUser(request));
	}
	/*
	* Obtiene el usuario mediante UUID
	*
	* */
	@GetMapping("/login/{id}")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id){
		return ResponseEntity.ok().body(this.userService.getUserById(id));
	}
	
}
