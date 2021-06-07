package org.pms.sammenu.repositories.essential_size;

import org.pms.sammenu.domain.forms.BalanceSheetDictionary;
import org.pms.sammenu.domain.forms.essential_size.base.MaterialityBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialityBaseRepository extends JpaRepository<MaterialityBase, Long> {

    List<MaterialityBase> findByAdd_Type(String balanceSheetType);

    List<MaterialityBase> findBySubtract_Type(String balanceSheetType);

    List<MaterialityBase> findByAdd_TypeAndAdd_Language(String balanceSheetType, String language);

    List<MaterialityBase> findBySubtract_TypeAndSubtract_Language(String balanceSheetType, String language);

    List<MaterialityBase> findByAdd(BalanceSheetDictionary add);

    List<MaterialityBase> findBySubtract(BalanceSheetDictionary subtract);

    @Query(value = "select m.* from materialitybase m " +
            "inner join base b on m.baseid = b.id " +
            "inner join isologismosdictionary d on (d.id = m.add or d.id = m.subtract) " +
            "where d.type = :balanceSheetType " +
            "and b.nlslang = :language and d.nlslang = :language and b.id = :baseId",
            nativeQuery = true)
    List<MaterialityBase> findByBalanceSheetTypeAndBaseId(@Param("balanceSheetType") String balanceSheetType,
                                                          @Param("language") String language,
                                                          @Param("baseId") Integer baseId);
}
