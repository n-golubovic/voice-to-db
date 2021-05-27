package net.etfbl.voicetodb.controllers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
import lombok.SneakyThrows;
import net.etfbl.voicetodb.models.JobSubmitResponse;
import net.etfbl.voicetodb.services.JobSubmitService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ws.schild.jave.EncoderException;

@WebMvcTest(JobSubmitController.class)
class JobSubmitControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockBean
   private JobSubmitService jobSubmitService;

   private static final ObjectMapper mapper = new ObjectMapper();

   private ListAppender<ILoggingEvent> listAppender;

   @BeforeEach
   void beforeEach() {
      Logger logger = (Logger) LoggerFactory.getLogger(JobSubmitController.class);
      listAppender = new ListAppender<>();
      listAppender.start();
      logger.addAppender(listAppender);
   }

   @SneakyThrows
   @Test
   void uploadFile_validRequest_return200AndValidJobSubmitResponse() {
      MockMultipartFile file1 = new MockMultipartFile("files", "file.wav", "audio/", new byte[]{'a'});

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart("/upload")
            .file(file1);

      when(jobSubmitService.save(any())).thenReturn("1234");

      MvcResult response = mockMvc.perform(request).andExpect(status().is(SC_OK)).andReturn();

      JobSubmitResponse result = mapper.readValue(response.getResponse().getContentAsString(), JobSubmitResponse.class);

      assertEquals("1234", result.getRequestId());
   }

   @SneakyThrows
   @Test
   void uploadFile_emptyFileRequest_return400AndEmptyResponse() {
      MockMultipartFile file1 = new MockMultipartFile("files", "file.wav", "audio/", new byte[]{});

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart("/upload")
            .file(file1);

      MvcResult response = mockMvc.perform(request).andExpect(status().is(SC_BAD_REQUEST)).andReturn();

      JobSubmitResponse result = mapper.readValue(response.getResponse().getContentAsString(), JobSubmitResponse.class);

      verify(jobSubmitService, never()).save(any());
      assertEquals(JobSubmitResponse.EMPTY, result);
   }

   @SneakyThrows
   @Test
   void uploadFile_notAudioFileRequest_return400AndEmptyResponse() {
      MockMultipartFile file1 = new MockMultipartFile("files", "file.wav", "text/", new byte[]{'a'});

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart("/upload")
            .file(file1);

      MvcResult response = mockMvc.perform(request).andExpect(status().is(SC_BAD_REQUEST)).andReturn();

      JobSubmitResponse result = mapper.readValue(response.getResponse().getContentAsString(), JobSubmitResponse.class);

      verify(jobSubmitService, never()).save(any());
      assertEquals(JobSubmitResponse.EMPTY, result);
   }


   @SneakyThrows
   @Test
   void uploadFile_onIoException_return500AndEmptyResponse() {
      MockMultipartFile file1 = new MockMultipartFile("files", "file.wav", "audio/", new byte[]{'a'});

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart("/upload")
            .file(file1);

      when(jobSubmitService.save(any())).thenThrow(IOException.class);

      MvcResult response = mockMvc.perform(request).andExpect(status().is(SC_INTERNAL_SERVER_ERROR)).andReturn();

      JobSubmitResponse result = mapper.readValue(response.getResponse().getContentAsString(), JobSubmitResponse.class);

      assertEquals(JobSubmitResponse.EMPTY, result);
      assertEquals(1, listAppender.list.size());
      assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
   }

   @SneakyThrows
   @Test
   void uploadFile_onEncoderException_return415AndEmptyResponse() {
      MockMultipartFile file1 = new MockMultipartFile("files", "file.wav", "audio/", new byte[]{'a'});

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart("/upload")
            .file(file1);

      when(jobSubmitService.save(any())).thenThrow(EncoderException.class);

      MvcResult response = mockMvc.perform(request).andExpect(status().is(SC_UNSUPPORTED_MEDIA_TYPE)).andReturn();

      JobSubmitResponse result = mapper.readValue(response.getResponse().getContentAsString(), JobSubmitResponse.class);

      assertEquals(JobSubmitResponse.EMPTY, result);
      assertEquals(1, listAppender.list.size());
      assertEquals(Level.ERROR, listAppender.list.get(0).getLevel());
   }

   @SneakyThrows
   @Test
   void uploadFile_noContentType_return400AndEmptyResponse() {
      MockMultipartFile file1 = new MockMultipartFile("files", "file.wav", null, new byte[]{'a'});

      MockHttpServletRequestBuilder request = MockMvcRequestBuilders
            .multipart("/upload")
            .file(file1);

      MvcResult response = mockMvc.perform(request).andExpect(status().is(SC_BAD_REQUEST)).andReturn();

      JobSubmitResponse result = mapper.readValue(response.getResponse().getContentAsString(), JobSubmitResponse.class);

      verify(jobSubmitService, never()).save(any());
      assertEquals(JobSubmitResponse.EMPTY, result);
   }


}
