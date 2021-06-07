package org.pms.sammenu.repositories.essential_size;

import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.essential_size.EssentialSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EssentialSizeRepository extends JpaRepository<EssentialSize, Long> {

    Optional<EssentialSize> findEssentialSizeByProject(Project project);
}
