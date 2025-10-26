package com.mentalapp.top.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class TopIndexControllerTest {
  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    TopIndexController topIndexController = new TopIndexController();
    mockMvc = MockMvcBuilders.standaloneSetup(topIndexController).build();
  }

  @Test
  void testShowIndex() throws Exception {
    mockMvc.perform(get("/")).andExpect(view().name("index")).andExpect(status().isOk());
  }
}
