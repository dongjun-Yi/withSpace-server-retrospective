package hansung.cse.withSpace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hansung.cse.withSpace.domain.Member;
import hansung.cse.withSpace.requestdto.friendship.FriendRequestDto;
import hansung.cse.withSpace.responsedto.friend.SendFriendShipResponseDto;
import hansung.cse.withSpace.service.FriendShipService;
import hansung.cse.withSpace.service.MemberService;
import hansung.cse.withSpace.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FriendShipController.class)
class FriendShipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FriendShipService friendShipService;

    @MockBean
    private RoomService roomService;
    @MockBean
    private MemberService memberService;

    @DisplayName("친구신청을 요청할 시 친구의 id는 필수값입니다.")
    @Test
    void sendFriendRequestWithoutFriendId() throws Exception {
        //given
        FriendRequestDto request = new FriendRequestDto(null);

        //when
        mockMvc.perform(post("/member/1/friend-request")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("친구의 번호는 필수입니다."));
    }


}