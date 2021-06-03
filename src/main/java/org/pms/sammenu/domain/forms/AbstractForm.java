package org.pms.sammenu.domain.forms;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.pms.sammenu.domain.Project;

import javax.persistence.*;

@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AbstractForm {

    @OneToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "projectid")
    protected Project project;
}
