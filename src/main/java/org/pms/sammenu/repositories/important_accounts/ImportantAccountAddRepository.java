package org.pms.sammenu.repositories.important_accounts;

import org.pms.sammenu.domain.forms.important_accounts.ImportantAccountAdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImportantAccountAddRepository extends JpaRepository<ImportantAccountAdd, Long> {
}
