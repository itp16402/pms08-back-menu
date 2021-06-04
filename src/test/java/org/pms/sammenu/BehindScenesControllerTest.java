package org.pms.sammenu;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {"spring.application.name=BehindScenesControllerTest",
        "spring.jmx.default-domain=BehindScenesControllerTest"})
public class BehindScenesControllerTest extends BasicWiremockTest {

    @Ignore
    @Test
    public void assignToManagerAllForms() throws Exception {


        this.mockMvc.perform(post("/behind-scenes/assign-admin/all-forms")
                .header(HEADER_KEY, HEADER_VALUE))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
