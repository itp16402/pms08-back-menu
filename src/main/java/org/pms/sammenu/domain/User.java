package org.pms.sammenu.domain;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "public.user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "username", unique=true)
	private String username;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@Column(name = "password")
	private String password;

	@Column(name = "active")
	private short active;

	@Column(name = "email")
	private String email;

	@ManyToMany(fetch= FetchType.EAGER)
	@JoinTable(name = "userrole", joinColumns =
	@JoinColumn(name = "userid"), inverseJoinColumns = @JoinColumn(name = "roleid")
	)
	private List<Authority> authorities;
}
