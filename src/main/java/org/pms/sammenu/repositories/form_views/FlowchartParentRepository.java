package org.pms.sammenu.repositories.form_views;

import org.pms.sammenu.domain.form_views.FlowchartParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowchartParentRepository extends JpaRepository<FlowchartParent, Long> {

    List<FlowchartParent> findByLanguageAndFormTypeOrderBySeqOrderAsc(String language, String formType);

    List<FlowchartParent> findByLanguageAndFormTypeOrFormTypeAndLanguageOrderBySeqOrderAsc(String languageT,
                                                                                           String formTypeT,
                                                                                           String formTypeF,
                                                                                           String languageF);
}
