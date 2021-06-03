package org.pms.sammenu.repositories.form_views;

import org.pms.sammenu.domain.form_views.FormList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FormListRepository extends JpaRepository<FormList, Long> {

    Optional<FormList> findByFormName(String formName);

    List<FormList> findByFormNameIn(Set<String> formNames);
}
