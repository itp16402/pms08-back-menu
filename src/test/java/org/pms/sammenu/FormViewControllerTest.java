package org.pms.sammenu;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.pms.sammenu.dto.form_views.FormViewDto;
import org.pms.sammenu.enums.FormType;
import org.pms.sammenu.enums.Locale;
import org.pms.sammenu.enums.OptionalStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {"spring.application.name=FormViewControllerTest",
        "spring.jmx.default-domain=FormViewControllerTest"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormViewControllerTest extends BasicWiremockTest {

    private static final String TABLE_NAME = "a111";
    private static final Long FLOWCHART_CHILD_ID = 25L;
    private static final String TYPO = "TITLE";
    private static final Locale LOCALE = Locale.el;
    private static final FormType FORMTYPE = FormType.T;

    @Ignore
    @Test
    public void a_saveFormView() throws Exception {

        FormViewDto formViewDto = buildFormView();

        this.mockMvc.perform(post("/form-views")
                .content(asJsonString(formViewDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Ignore
    @Test
    public void b_findFormsByTableNameAndFormType() throws Exception {
        this.mockMvc.perform(get("/form-views/form-type/{tableName}/{locale}/{formType}",
                TABLE_NAME, LOCALE, FORMTYPE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Ignore
    @Test
    public void c_findFormsByTableNameAndOrderId() throws Exception {
        this.mockMvc.perform(get("/form-views/order/{tableName}/{locale}/{projectId}",
                TABLE_NAME, LOCALE, PROJECT_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void d_findAllTableNames() throws Exception {
        this.mockMvc.perform(get("/form-views/table-names"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Ignore
    @Test
    public void e_findFormByTableNameAndTypo() throws Exception {
        this.mockMvc.perform(get("/form-views/{tableName}/{locale}/{projectId}/{typo}",
                TABLE_NAME, LOCALE, PROJECT_ID, TYPO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void f_findFormsByFlowchartChildIdAndTypo() throws Exception {
        this.mockMvc.perform(get("/form-views/flowchart-child/{flowchartChildId}/{locale}/{typo}/{projectId}",
                FLOWCHART_CHILD_ID, LOCALE, TYPO, PROJECT_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void f_findFormsByFlowchartIdAndTypo() throws Exception {
        this.mockMvc.perform(get("/form-views/flowchart/{flowchartId}/{locale}/{typo}/{projectId}",
                104L, LOCALE, TYPO, PROJECT_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void g_isFormTypeOta() throws Exception {
        this.mockMvc.perform(get("/form-views/check/form-type/OTA/{projectId}", PROJECT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private FormViewDto buildFormView(){

        return FormViewDto.builder()
                .id(8642L)
                .tableName("a111")
                .language(Locale.el)
                .formType(FormType.T)
                .keli("a6")
                .onoma("Ημερομηνία επιστολής ανάθεσης")
                .typos("DATE")
                .sValues("")
                .infos("")
                .cell("")
                .sFunction("")
                .css("")
                .sOrder(6f)
                .sPrint(0)
                .optional(OptionalStatus.MANDATORY)
                .comments("OK")
                .build();
    }
}
