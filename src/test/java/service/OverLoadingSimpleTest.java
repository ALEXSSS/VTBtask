package service;

import com.websystique.springmvc.configuration.JPAConfig;
import com.websystique.springmvc.configuration.MainApplicationConfiguration;
import com.websystique.springmvc.controller.MainController;
import com.websystique.springmvc.model.DBEntry;
import com.websystique.springmvc.repo.DBEntryRepository;
import com.websystique.springmvc.service.DBEntryBufferService;
import com.websystique.springmvc.service.EntryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MainApplicationConfiguration.class, JPAConfig.class})
@WebAppConfiguration
public class OverLoadingSimpleTest {


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dbEntryBufferService).build();
    }

    private MockMvc mockMvc;

    @Mock
    private DBEntryRepository repository;

    @InjectMocks
    private DBEntryBufferService dbEntryBufferService;

    @Test
    public void queueMultiThreadingTest() throws InterruptedException {
        Mockito.when(repository.saveAll( Matchers.<Iterable<? extends DBEntry>>any()))
                .thenReturn(null);
        List<Thread> executionPool = new ArrayList<>();

        for (int thread_num = 0; thread_num < 20; thread_num++) {
            executionPool.add(new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    DBEntry re = new DBEntry(1,2,"TXT","DATE");
                    dbEntryBufferService.add(re);
                }
            }));
        }
        dbEntryBufferService.add(new DBEntry());
        executionPool.forEach(thread -> thread.start());
        for (Thread thread : executionPool) {
            thread.join();
        }
        Assert.assertEquals("Expected 0", 0, dbEntryBufferService.getBufferSize());
    }

    @Test
    public void queueMultiThreadingTest2() throws InterruptedException {
        Mockito.when(repository.saveAll( Matchers.<Iterable<? extends DBEntry>>any()))
                .thenReturn(null);
        List<Thread> executionPool = new ArrayList<>();

        for (int thread_num = 0; thread_num < 20; thread_num++) {
            executionPool.add(new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    DBEntry re = new DBEntry(1,2,"TXT","DATE");
                    dbEntryBufferService.add(re);
                }
            }));
        }
        dbEntryBufferService.add(new DBEntry());
        executionPool.forEach(thread -> thread.start());
        for (Thread thread : executionPool) {
            thread.join();
        }
        Assert.assertEquals("Expected 0", 0, dbEntryBufferService.getBufferSize());
    }

    @Test
    public void queueMultiThreadingTest3() throws InterruptedException {
        Mockito.when(repository.saveAll( Matchers.<Iterable<? extends DBEntry>>any()))
                .thenReturn(null);
        List<Thread> executionPool = new ArrayList<>();

        for (int thread_num = 0; thread_num < 20; thread_num++) {
            executionPool.add(new Thread(() -> {
                for (int i = 0; i < 10000; i++) {
                    DBEntry re = new DBEntry(1,2,"TXT","DATE");
                    dbEntryBufferService.add(re);
                }
            }));
        }
        dbEntryBufferService.add(new DBEntry());
        executionPool.forEach(thread -> thread.start());
        for (Thread thread : executionPool) {
            thread.join();
        }
        Assert.assertEquals("Expected 0", 1, dbEntryBufferService.getBufferSize());
    }
}
