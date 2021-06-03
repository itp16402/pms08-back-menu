package org.pms.sammenu;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.pms.sammenu.dto.form_views.FormStatusRequestDto;
import org.pms.sammenu.enums.FormStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {"spring.application.name=FormListControllerTest",
        "spring.jmx.default-domain=FormListControllerTest"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FormListControllerTest extends BasicWiremockTest {

    private final String FORM_NAME = "a111";

    @Test
    public void a_findAll() throws Exception {
        this.mockMvc.perform(get("/form-lists"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    public void b_findFormsByTableName() throws Exception {
        this.mockMvc.perform(get("/form-lists/{formName}", FORM_NAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Ignore
    @Test
    public void c_saveStatus() throws Exception {

        FormStatusRequestDto formStatusRequestDto = FormStatusRequestDto.builder().status(FormStatus.PROCESSED).build();

        this.mockMvc.perform(post("/form-lists/status/{formListId}/{projectId}",
                7L, PROJECT_ID)
                .header(HEADER_KEY, HEADER_VALUE)
                .content(asJsonString(formStatusRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Ignore
    @Test
    public void d_getStatus() throws Exception {
        this.mockMvc.perform(get("/form-lists/status/{orderId}/{projectId}",
                PROJECT_ID, 7L)
                .header(HEADER_KEY, HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
