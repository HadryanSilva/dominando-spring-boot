package br.com.hadryan.controller;

import br.com.hadryan.ProducerMapper;
import br.com.hadryan.domain.Producer;
import br.com.hadryan.repository.ProducerData;
import br.com.hadryan.repository.ProducerHardCodedRepository;
import br.com.hadryan.request.ProducerPostRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(ProducerController.class)
class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProducerData producerData;
    @SpyBean
    private ProducerHardCodedRepository repository;
    @SpyBean
    private ProducerMapper mapper;
    private List<Producer> producers;

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    void init() {
        var toei = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
        producers = new ArrayList<>(List.of(toei, witStudio, studioGhibli));

        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
    }

    @Test
    @DisplayName("findAll() returns a list with all producers")
    void findAll_ReturnsAllProducers_WhenSuccessful() throws Exception {
        var response = readResourceFile("producer/get-producer-null-name-200.json");
        mockMvc.perform(get("/v1/producers"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findAll() returns list with filtered producers when name is not null")
    void findAll_ReturnsFilteredProducer_WhenNameIsNotNull() throws Exception {
        var response = readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "Ufotable";
        mockMvc.perform(get("/v1/producers").param("name", name))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findAll() returns a list with all producers")
    void findAll_ReturnsEmptyList_WhoNonNameIsFound() throws Exception {
        var response = readResourceFile("producer/get-producer-x-name-200.json");
        var name = "Teste";
        mockMvc.perform(get("/v1/producers").param("name", name))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findById() return 404 when producer not found")
    void findById_Returns404_WhenProducerNotFound() throws Exception {
        mockMvc.perform(get("/v1/producers/{id}", 4L))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("findById() returns a empty list with all producers")
    void findById_ReturnsProducer_WhenSuccessful() throws Exception {
        var response = readResourceFile("producer/get-producer-ufotable-id-200.json");
        mockMvc.perform(get("/v1/producers/{id}", 1L))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }



    @Test
    @DisplayName("save() creates a Producer when successful")
    void save_CreatesProducer_WhenSuccessful() throws Exception {
        var request = readResourceFile("producer/post-request-producer-200.json");
        var response = readResourceFile("producer/post-response-producer-201.json");

        var producerToBeSaved = Producer.builder()
                .id(34219L)
                .name("Aniplex")
                .createdAt(LocalDateTime.now())
                .build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToBeSaved);
        BDDMockito.when(mapper.toProducer((ProducerPostRequest) ArgumentMatchers.any())).thenReturn(producerToBeSaved);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("update() updates a Producer when successful")
    void update_UpdatesProducer_WhenSuccessful() throws Exception {
        var request = readResourceFile("producer/put-request-producer-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("update() throw ResponseStatusException when producer not found")
    void update_UpdatesThrowResponseStatusException_WhenProducerNotFound() throws Exception {
        var request = readResourceFile("producer/put-request-producer-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/v1/producers")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Cannot found producer to be updated"));
    }

    @Test
    @DisplayName("delete() removes a Producer when successful")
    void delete_deletesProducer_WhenSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", 1L))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("delete() throw ResponseStatusException when producer not found")
    void delete_RemovesThrowResponseStatusException_WhenProducerNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", 1000))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Cannot found producer to be deleted"));
    }

    private String readResourceFile(String filename) throws Exception {
        var file = resourceLoader.getResource("classpath:%s".formatted(filename)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}