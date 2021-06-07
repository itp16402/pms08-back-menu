package org.pms.sammenu.repositories;

import org.pms.sammenu.domain.forms.BalanceSheetDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceSheetDictionaryRepository extends JpaRepository<BalanceSheetDictionary, Long> {
}
