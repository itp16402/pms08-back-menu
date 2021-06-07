package org.pms.sammenu.repositories.essential_size;

import org.pms.sammenu.domain.forms.essential_size.EssentialSize;
import org.pms.sammenu.domain.forms.essential_size.EssentialSizePerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EssentialSizePerformanceRepository extends JpaRepository<EssentialSizePerformance, Long> {

    List<EssentialSizePerformance> findByEssentialSize_Project_Id(Long projectId);

    Optional<EssentialSizePerformance> findByEssentialSize(EssentialSize essentialSize);
}
