package com.app.springtest.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void greetingShouldReturnDefaultMessage() throws Exception {
//        assertThat(
//                this.restTemplate.getForObject("http://localhost:" + port + "/", String.class)).contains("Hello, World");
//    }

//    @Test
//    void shouldReturnDefaultMessage() throws Exception {
//        this.mockMvc.perform(get("/api/member/1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString("Hello, World")));
//    }

}
