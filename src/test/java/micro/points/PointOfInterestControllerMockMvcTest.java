package micro.points;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import micro.points.location.Location;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.OptimisticLockException;
import java.util.UUID;

import static micro.points.PointsFixture.Locations.rooseveltlaanInGent;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointOfInterestController.class)
class PointOfInterestControllerMockMvcTest {

    UUID givenId = UUID.randomUUID();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PointOfInterestService service;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getExisting() throws Exception {
        existingPoint(PointsFixture.example());

        mockMvc.perform(
                get("/poi/{id}", givenId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getNotExisting() throws Exception {
        noPointExists();

        mockMvc.perform(
                get("/poi/{id}", givenId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchExisting() throws Exception {
        Point givenPoint = PointsFixture.example().setLocation(rooseveltlaanInGent());
        whenUpdating().thenReturn(givenPoint);

        mockMvc.perform(
                patch("/poi/{id}", givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(rooseveltlaanInGent()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOf(givenPoint)));

        Mockito.verify(service, Mockito.times(1))
                .update(givenId, PointOfInterestService.PointUpdate.location(rooseveltlaanInGent()));
    }

    @Test
    void patchNotExisting() throws Exception {
        whenUpdating().thenThrow(new NotExists());

        mockMvc.perform(
                patch("/poi/{id}", givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(rooseveltlaanInGent()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Mockito.verify(service, Mockito.times(1))
                .update(givenId, PointOfInterestService.PointUpdate.location(rooseveltlaanInGent()));
    }

    @Test
    void patchRetryAfterOptimisticLock() throws Exception {
        Point givenPoint = PointsFixture.example().setLocation(rooseveltlaanInGent());

        whenUpdating()
                .thenThrow(new OptimisticLockException())
                .thenReturn(givenPoint);

        mockMvc.perform(
                patch("/poi/{id}", givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOf(rooseveltlaanInGent()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(2))
                .update(givenId, PointOfInterestService.PointUpdate.location(rooseveltlaanInGent()));
    }

    @Test
    void patchRetryMultipleTimeAndRethrow() throws Exception {
        Point givenPoint = PointsFixture.example().setLocation(rooseveltlaanInGent());

        whenUpdating()
                .thenThrow(
                        new OptimisticLockException(),
                        new OptimisticLockException(),
                        new OptimisticLockException()
                ).thenReturn(givenPoint);

        Assertions.assertThatThrownBy(() ->
                mockMvc.perform(
                        patch("/poi/{id}", givenId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonOf(rooseveltlaanInGent()))
                                .accept(MediaType.APPLICATION_JSON))
        ).hasCauseInstanceOf(OptimisticLockException.class);

        Mockito.verify(service, Mockito.times(3))
                .update(givenId, PointOfInterestService.PointUpdate.location(rooseveltlaanInGent()));
    }

    private void noPointExists() {
        Mockito.when(service.findById(Mockito.any(UUID.class))).thenThrow(new NotExists());
    }

    private void existingPoint(Point point) {
        Mockito.when(service.findById(givenId)).thenReturn(point);
    }

    private OngoingStubbing<Point> whenUpdating() {
        return Mockito.when(service.update(Mockito.eq(givenId), Mockito.any()));
    }

    private String jsonOf(Location location) throws JsonProcessingException {
        return mapper.writeValueAsString(PointOfInterestService.PointUpdate.location(location));
    }

    private String jsonOf(Point point) throws JsonProcessingException {
        return mapper.writeValueAsString(point);
    }
}
