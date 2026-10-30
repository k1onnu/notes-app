package com.k1onnu.notes_app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ErrorHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetNoteById_NotFound_Returns404AndErrorResponse() throws Exception {
        mockMvc.perform(get("/api/notes/9999"))
                .andExpect(status().isNotFound()) // Ожидаем 404
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }
}
