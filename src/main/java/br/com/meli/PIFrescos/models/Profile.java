package br.com.meli.PIFrescos.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Juliano Alcione de Souza
 */
@Entity
@Getter
@Setter
public class Profile implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;

	@Override
	public String getAuthority() {
		return name;
	}
	
}
