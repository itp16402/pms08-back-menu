package org.pms.sammenu.repositories.essential_size;

import org.pms.sammenu.domain.forms.essential_size.EssentialSizeOverall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EssentialSizeOverallRepository extends JpaRepository<EssentialSizeOverall, Long> {
}
