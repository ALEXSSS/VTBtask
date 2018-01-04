package controller;

import com.websystique.springmvc.configuration.JPAConfig;
import com.websystique.springmvc.configuration.MainApplicationConfiguration;
import com.websystique.springmvc.controller.MainController;
import com.websystique.springmvc.model.DBEntry;
import com.websystique.springmvc.repo.DBEntryRepository;
import com.websystique.springmvc.service.EntryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplicationConfiguration.class, JPAConfig.class})
@WebAppConfiguration
public class TestMainController {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    private MockMvc mockMvc;

    @InjectMocks
    private MainController mainController;

    @Mock
    EntryService entryService;

    @Autowired
    DBEntryRepository repository;

    @Test
    public void testEntryGet() throws Exception {

        List<DBEntry> response_data = new ArrayList<>();
        response_data.add(new DBEntry(1, 1, "STR", "DATE"));

        Mockito.when(entryService.findAllEntries())
                .thenReturn(response_data);
        MvcResult result = mockMvc.perform(get("/VTB/entry/"))
                .andExpect(status().isOk()).andReturn();
        verify(entryService, times(1)).findAllEntries();
        verifyNoMoreInteractions(entryService);
    }

    @Test
    public void testEntryGetNotFound() throws Exception {

        List<DBEntry> response_data = new ArrayList<>();

        repository.save(new DBEntry(1, 1, "dsf", "dsf"));
        Mockito.when(entryService.findAllEntries())
                .thenReturn(response_data);
        MvcResult result = mockMvc.perform(get("/VTB/entry/"))
                .andExpect(status().isNoContent()).andReturn();
        verify(entryService, times(1)).findAllEntries();
        verifyNoMoreInteractions(entryService);
    }


}

