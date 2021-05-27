package net.etfbl.voicetodb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import lombok.SneakyThrows;
import net.etfbl.voicetodb.components.ResultStorage;
import net.etfbl.voicetodb.models.ResultResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResultController.class)
class ResultControllerTest {

   private static final ObjectMapper mapper = new ObjectMapper();

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private ResultStorage resultStorage;


   @SneakyThrows
   @Test
   void getResult_validRequest_return200AndResultString() {
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/result")
            .param("requestId", "1234");

      when(resultStorage.get("1234")).thenReturn(Optional.of("result."));

      MvcResult result = mockMvc.perform(request)
            .andExpect(status().is(SC_OK))
            .andReturn();


      ResultResponse resultResponse =  mapper.readValue(result.getResponse().getContentAsString(), ResultResponse.class);

      assertEquals("result.", resultResponse.getText());
   }

   @SneakyThrows
   @Test
   void getResult_nonExistantRequestId_return404AndEmptyResult() {
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .get("/result")
            .param("requestId", "notReal");

      when(resultStorage.get("notReal")).thenReturn(Optional.empty());

      MvcResult result = mockMvc.perform(request)
            .andExpect(status().is(SC_NOT_FOUND))
            .andReturn();

      ResultResponse resultResponse = mapper.readValue(result.getResponse().getContentAsString(), ResultResponse.class);

      assertEquals(ResultResponse.EMPTY, resultResponse);
   }
}
