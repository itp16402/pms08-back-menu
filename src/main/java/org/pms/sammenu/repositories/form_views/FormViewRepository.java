package org.pms.sammenu.repositories.form_views;

import org.pms.sammenu.domain.form_views.FormView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormViewRepository extends JpaRepository<FormView, Long> {

    List<FormView> findByTableNameAndLanguageAndFormTypeOrderBySeqOrderAsc(String tableName,
                                                                           String language,
                                                                           String formType);

    Optional<FormView> findByTableNameAndLanguageAndFormTypeAndKeli(String tableName,
                                                                    String language,
                                                                    String formType,
                                                                    String keli);

    Optional<FormView> findByTableNameAndLanguageAndFormTypeAndKeliAndInfos(String tableName,
                                                                            String language,
                                                                            String formType,
                                                                            String keli,
                                                                            String infos);

    Optional<FormView> findByTableNameAndLanguageAndFormTypeAndTypos(String tableName,
                                                                     String language,
                                                                     String formType,
                                                                     String typos);

    @Query(value = "select * from forms f2 where tablename in " +
            "(SELECT s.token FROM flowchartchild t, unnest(string_to_array(t.formname, ',')) " +
            "s(token) WHERE  id = :flowchartChildId) " +
            "and typos = :typo and nlslang = :language and formtype = :formType order by sorder asc",
            nativeQuery = true)
    List<FormView> findByFlowchartChildIdAndLanguageAndFormTypeAndTypos(@Param("flowchartChildId") Long flowchartChildId,
                                                                        @Param("typo") String typo,
                                                                        @Param("language") String language,
                                                                        @Param("formType") String formType);

    @Query(value = "select * from forms f2 where tablename in " +
            "(SELECT s.token FROM flowchart t, unnest(string_to_array(t.formname, '/')) " +
            "s(token) WHERE  id = :flowchartId) " +
            "and typos = :typo and nlslang = :language and formtype = :formType order by sorder asc",
            nativeQuery = true)
    List<FormView> findByFlowchartIdAndLanguageAndFormTypeAndTypos(@Param("flowchartId") Long flowchartId,
                                                                   @Param("typo") String typo,
                                                                   @Param("language") String language,
                                                                   @Param("formType") String formType);

    @Query(value = "SELECT distinct(tablename) FROM forms", nativeQuery = true)
    List<String> findAllTableNames();
}