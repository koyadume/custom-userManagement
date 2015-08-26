package org.koyad.piston.app.userMgmt.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Role extends AbstractEntity {
	private String name;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "USER_ROLE",
			joinColumns = @JoinColumn( name="ROLE_NAME"),
            inverseJoinColumns = @JoinColumn(name="UID")
	)
	private Set<String> members;
}
