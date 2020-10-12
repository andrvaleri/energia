package ee.energia.metal;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class RestApiIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void testRestApiResponds() throws Exception {
        mvc.perform(get("/api/v1/purchases")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MvcResult result = mvc.perform(post("/api/v1/purchases/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("V,A,L,E,R,I"))
                .andExpect(status().isOk()).andExpect(content().json("{\"bakeGoods\"" +
                        ":[{\"name\":\"Brownie\"}," +
                        "{\"name\":\"Muffin\"}," +
                        "{\"name\":\"Cake Pop\"}," +
                        "{\"name\":\"Water\"}," +
                        "{\"name\":\"Cola\"}," +
                        "{\"name\":\"Caramel\"}]," +
                        "\"price\":7.50," +
                        "\"currency\":\"$\"," +
                        "\"status\":\"INITIATED\"," +
                        "\"_links\":{}}}"))
                .andReturn();
        int id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        mvc.perform(post("/api/v1/purchases/" + id + "/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content("$8"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"bakeGoods\"" +
                        ":[{\"name\":\"Brownie\"}," +
                        "{\"name\":\"Muffin\"}," +
                        "{\"name\":\"Cake Pop\"}," +
                        "{\"name\":\"Water\"}," +
                        "{\"name\":\"Cola\"}," +
                        "{\"name\":\"Caramel\"}]," +
                        "\"price\":7.50," +
                        "\"change\":0.5," +
                        "\"currency\":\"$\"," +
                        "\"status\":\"COMPLETED\"}"));
        mvc.perform(post("/api/v1/purchases/" + id + "/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .content("$8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"title\":Error," +
                        "\"detail\":\"Purchase is already completed\"," +
                        "\"status\":400}"));
    }

}
