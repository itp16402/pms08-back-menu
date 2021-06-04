package org.pms.sammenu.dto.form_roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentPhaseDto {

    private Short phase;
    private String name;
    private float sOrder;
    private boolean checked;
    private List<PhaseDto> phases;
}
