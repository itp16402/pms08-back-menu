package org.pms.sammenu.repositories.form_views;

import org.pms.sammenu.domain.form_views.Flowchart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowchartRepository extends JpaRepository<Flowchart, Long> {

    List<Flowchart> findByLanguageAndFormTypeOrderBySeqOrderAsc(String language, String formType);
}
