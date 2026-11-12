package com.k1onnu.notes_app.controller;

import com.k1onnu.notes_app.dto.NoteRequest;
import com.k1onnu.notes_app.dto.NoteResponse;
import com.k1onnu.notes_app.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void createNote_ShouldReturn400_WhenTitleIsBlank() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("");
        request.setContent("Some content");

        mockMvc.perform(post("/api/notes")
                        .principal(() -> "testuser") // ВОТ ИСПРАВЛЕНИЕ: передаем Principal напрямую
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createNote_ShouldReturn201_WhenValid() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("Valid Title");
        request.setContent("Content");

        NoteResponse mockResponse = new NoteResponse();
        mockResponse.setId(1L);
        mockResponse.setTitle("Valid Title");

        when(noteService.createNote(any(), anyString())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/notes")
                        .principal(() -> "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}