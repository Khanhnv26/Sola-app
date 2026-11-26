package com.sola.station;

import com.sola.common.AppException;
import com.sola.common.ErrorCode;
import com.sola.common.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicStationController.class)
@Import(GlobalExceptionHandler.class)
class PublicStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StationQueryService stationQueryService;

    @Test
    void listFeaturedStations_returns200AndSuccessCode() throws Exception {
        StationSummaryResponse sample = new StationSummaryResponse(
                UUID.randomUUID(),
                "solar-morning",
                "Solar Morning",
                "Nhịp lo-fi sáng rõ kết hợp tiếng mưa dịu nhẹ.",
                "LOFI",
                25,
                "solar",
                true,
                null,
                "Chillhop FM",
                1280L,
                List.of()
        );
        when(stationQueryService.listFeaturedStations()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/v1/public/stations/featured"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data[0].slug").value("solar-morning"));
    }

    @Test
    void getStationBySlug_notFoundReturns404AndStationNotFound() throws Exception {
        when(stationQueryService.getPublicStationBySlug("khong-ton-tai"))
                .thenThrow(new AppException(ErrorCode.STATION_NOT_FOUND));

        mockMvc.perform(get("/api/v1/public/stations/khong-ton-tai"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("STATION_NOT_FOUND"));
    }

    @Test
    void discoverStations_sizeTooLargeReturns400AndValidationError() throws Exception {
        mockMvc.perform(get("/api/v1/public/stations/discover").param("size", "500"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
