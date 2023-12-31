package br.com.hadryan.controller;

import br.com.hadryan.AnimeMapper;
import br.com.hadryan.commons.AnimeUtils;
import br.com.hadryan.commons.FileUtils;
import br.com.hadryan.domain.Anime;
import br.com.hadryan.repository.AnimeData;
import br.com.hadryan.repository.AnimeHardCodedRepository;
import br.com.hadryan.request.AnimePostRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeData animeData;
    private List<Anime> animes;
    @SpyBean
    private AnimeMapper animeMapper;
    @SpyBean
    private AnimeHardCodedRepository repository;
    @InjectMocks
    private AnimeUtils animeUtils;
    @Autowired
    private FileUtils fileUtils;

    private static final String URL = "/v1/animes";

    @BeforeEach
    void init() {
        animes = animeUtils.newAnimeList();
        BDDMockito.when(animeData.getAnimes()).thenReturn(animes);
    }

    @Test
    @DisplayName("findAll() must return a list with all animes when name is not passed")
    void findAll_ReturnsAllAnimes_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");
        mockMvc.perform(MockMvcRequestBuilders
                    .get(URL))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findAll() return the anime with given name")
    void findAll_ReturnsAnime_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-naruto-name-200.json");
        var name = "Naruto";
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findAll() returns an empy list when name not found ")
    void findAll_ReturnsEmptyList_WhenNameNotFound() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        var name = "Teste";
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .param("name", name))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("findById() returns Anime with given id")
    void findById_ReturnsAnime_WhenSuccessful() throws Exception {
        var id = 1L;
        var response = fileUtils.readResourceFile("anime/get-anime-naruto-id-200.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("findById() return 404 when anime not found")
    void findById_Returns404_WhenAnimeNotFound() throws Exception {
        var id = 4L;
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("save() returns Anime when successful")
    void save_ReturnsAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/post-request-anime-201.json");
        var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");

        Anime animeToBeSaved = Anime.builder().id(4L).name("One Piece").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToBeSaved);
        BDDMockito.when(animeMapper.toAnime((AnimePostRequest) ArgumentMatchers.any())).thenReturn(animeToBeSaved);

        mockMvc.perform(MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andDo(print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("update() must update an Anime with given id")
    void update_UpdatesAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-204.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("update() throw ResponseStatusException when anime not found")
    void update_ThrowResponseStatusException_WhenAnimeNotFound() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Cannot found anime to be deleted"));
    }

    @Test
    @DisplayName("delete() must delete Anime when successful")
    void delete_DeleteAnime_WhenSuccessful() throws Exception {
        var id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("delete() throw ResponseStatusException when anime not found")
    void delete_ThrowResponseStatusException_WhenAnimeNotFound() throws Exception {
        var id = 4L;
        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Cannot found anime to be deleted"));
    }
}