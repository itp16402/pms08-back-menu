package org.pms.sammenu.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Authority {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "role")
	private String description;
}
