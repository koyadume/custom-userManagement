package org.koyad.piston.app.userMgmt.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="GROUPS")
public class Group extends AbstractEntity {
	private String name;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "USER_GROUP",
			joinColumns = @JoinColumn( name="GROUP_NAME"),
            inverseJoinColumns = @JoinColumn(name="USER_UID")
	)
	private Set<String> members;
}
