package org.pms.sammenu.repositories.important_accounts;

import org.pms.sammenu.domain.Project;
import org.pms.sammenu.domain.forms.important_accounts.ImportantAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImportantAccountRepository extends JpaRepository<ImportantAccount, Long> {
    Optional<ImportantAccount> findImportantAccountByProject(Project project);
}
