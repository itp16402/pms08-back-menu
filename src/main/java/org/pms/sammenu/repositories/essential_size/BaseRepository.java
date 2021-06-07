package org.pms.sammenu.repositories.essential_size;

import org.pms.sammenu.domain.forms.essential_size.base.Base;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseRepository extends JpaRepository<Base, Integer> {

    @Query(value = "select b.id, b.name, b.nlslang from base b " +
            "inner join materialitybase m on m.baseid = b.id " +
            "inner join isologismosdictionary d on (d.id = m.add or d.id = m.subtract) " +
            "where d.type = :balanceSheetType " +
            "and b.nlslang = :language and d.nlslang = :language " +
            "group by b.id",
            nativeQuery = true)
    List<Base> findByBalanceSheetType(@Param("balanceSheetType") String balanceSheetType,
                                      @Param("language") String language);
}
