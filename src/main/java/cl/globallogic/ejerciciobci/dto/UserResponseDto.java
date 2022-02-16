package cl.globallogic.ejerciciobci.dto;


import cl.globallogic.ejerciciobci.entity.Phone;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

	private String idUser;
	private String name;
	private String email;
	private String password;
	private Set<Phone> phones;
	private LocalDateTime created;
	private LocalDateTime lastLogin;
	private String token;
	private boolean isActive;
}
