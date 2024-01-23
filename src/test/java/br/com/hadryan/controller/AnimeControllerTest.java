package br.com.hadryan.controller;

import br.com.hadryan.commons.AnimeUtils;
import br.com.hadryan.commons.FileUtils;
import br.com.hadryan.domain.Anime;
import br.com.hadryan.mapper.AnimeMapper;
import br.com.hadryan.mapper.AnimeMapperImpl;
import br.com.hadryan.repository.AnimeData;
import br.com.hadryan.repository.AnimeHardCodedRepository;
import br.com.hadryan.request.AnimePostRequest;
import br.com.hadryan.service.AnimeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AnimeController.class)
@Import({AnimeMapperImpl.class, FileUtils.class,
        AnimeUtils.class, AnimeService.class})
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
    @DisplayName("save() returns BadRequest when name is blank")
    void save_ReturnsBadRequest_WhenNameIsBlank() throws Exception {
        var request = fileUtils.readResourceFile("anime/post-request-anime-blank-400.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
       var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

       var resolvedException = result.getResolvedException();

       Assertions.assertThat(resolvedException).isNotNull();
       Assertions.assertThat(resolvedException.getMessage())
               .containsIgnoringCase("Cannot found anime to be deleted");
    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSourceFiles")
    @DisplayName("save() returns BadRequest when name is null")
    void save_ReturnsBadRequest_WhenNameIsNull(String filename, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(filename));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage())
                .containsIgnoringCase(errors.get(0));
    }

    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSourceFiles")
    @DisplayName("update() returns BadRequest when name is empty")
    void update_ReturnsBadRequest_WhenNameIsEmpty(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).containsIgnoringCase(errors.get(0));
    }

    private static Stream<Arguments> postAnimeBadRequestSourceFiles() {
        var allErrorsMap = getMessageErrors();
        var nameError = Collections.singletonList(allErrorsMap.get("name"));

        return Stream.of(
                Arguments.of("post-request-anime-blank-400.json", nameError),
                Arguments.of("post-request-anime-null-400.json", nameError)
        );
    }

    private static Stream<Arguments> putAnimeBadRequestSourceFiles() {
        var allErrorsMap = getMessageErrors();
        var nameError = Collections.singletonList(allErrorsMap.get("name"));

        return Stream.of(
                Arguments.of("put-request-anime-blank-400.json", nameError),
                Arguments.of("put-request-anime-null-400.json", nameError)
        );
    }

    private static Map<String, String> getMessageErrors() {
        var nameError = "The field 'name' is required";
        var errors = new HashMap<String, String>();

        errors.put("name", nameError);
        return errors;
    }
}