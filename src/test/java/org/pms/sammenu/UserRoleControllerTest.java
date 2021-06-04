package org.pms.sammenu;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.pms.sammenu.dto.UserRoleRequestDto;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {"spring.application.name=UserRoleControllerTest",
        "spring.jmx.default-domain=UserRoleControllerTest"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRoleControllerTest extends BasicWiremockTest {

    @Test
    public void a_fetchMemberRolesByOrderId() throws Exception {
        this.mockMvc.perform(get("/user-roles/{projectId}", PROJECT_ID).header(HEADER_KEY, HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Ignore
    @Test
    public void b_addMembers() throws Exception {

        List<UserRoleRequestDto> userRoleRequestDtoList = buildUserRoles();

        this.mockMvc.perform(post("/user-roles/{projectId}", PROJECT_ID).header(HEADER_KEY, HEADER_VALUE)
                .content(asJsonString(userRoleRequestDtoList)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Ignore
    @Test
    public void d_removeMember() throws Exception {
        this.mockMvc.perform(
                delete("/user-roles/remove/member/{memberId}/{projectId}",
                        5L, PROJECT_ID).header(HEADER_KEY, HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Ignore
    @Test
    public void e_assignAdmin() throws Exception {

        this.mockMvc.perform(post("/user-roles/assign-admin/{userId}/{projectId}",
                5L, PROJECT_ID).header(HEADER_KEY, HEADER_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Ignore
    @Test
    public void f_removeAdmin() throws Exception {
        this.mockMvc.perform(
                delete("/user-roles/remove/admin/{userId}/{projectId}",
                        5L, PROJECT_ID).header(HEADER_KEY, HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private List<UserRoleRequestDto> buildUserRoles(){

        List<UserRoleRequestDto> memberRoleRequestDtos = new ArrayList<>();

        memberRoleRequestDtos.add(UserRoleRequestDto.builder().userId(5L).roleId((short) 3).build());

        return memberRoleRequestDtos;
    }
}
