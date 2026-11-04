package com.k1onnu.notes_app.controller;

import tools.jackson.databind.json.JsonMapper;
import com.k1onnu.notes_app.dto.NoteRequest;
import com.k1onnu.notes_app.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private NoteService noteService;

    // Проверка, что валидация работает
    @Test
    void createNote_ShouldReturn400_WhenTitleIsBlank() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("");
        request.setContent("Some content");

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // Проверка успешного создания (201 Created)
    @Test
    void createNote_ShouldReturn201_WhenValid() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("Valid Title");
        request.setContent("Content");

        when(noteService.createNote(any())).thenReturn(null);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}