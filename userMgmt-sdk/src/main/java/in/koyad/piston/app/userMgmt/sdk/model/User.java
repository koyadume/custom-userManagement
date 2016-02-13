package in.koyad.piston.app.userMgmt.sdk.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.koyad.piston.core.model.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class User extends AbstractEntity {
	private String uid;
	private String password;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	private String email;
	
}
