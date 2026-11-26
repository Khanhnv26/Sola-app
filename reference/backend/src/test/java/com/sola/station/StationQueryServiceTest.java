package com.sola.station;

import com.sola.common.AppException;
import com.sola.common.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationQueryServiceTest {

    @Mock
    private AmbientSoundRepository ambientSoundRepository;

    @Mock
    private MoodStationRepository moodStationRepository;

    @Mock
    private StationSourceRepository stationSourceRepository;

    @Mock
    private StationAmbientLayerRepository stationAmbientLayerRepository;

    @InjectMocks
    private StationQueryService stationQueryService;

    @Test
    void resolveMoodTags_focusMapsToDeepWorkAndCoding() {
        Set<String> tags = stationQueryService.resolveMoodTags("FOCUS");
        assertThat(tags).containsExactlyInAnyOrder("DEEP_WORK", "CODING");
    }

    @Test
    void resolveMoodTags_invalidCategoryThrowsValidationError() {
        assertThatThrownBy(() -> stationQueryService.resolveMoodTags("abc"))
                .isInstanceOf(AppException.class)
                .extracting(ex -> ((AppException) ex).getErrorCode())
                .isEqualTo(ErrorCode.VALIDATION_ERROR);
    }

    @Test
    void getPublicStationBySlug_missingSlugThrowsStationNotFound() {
        when(moodStationRepository.findBySlug("khong-ton-tai")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stationQueryService.getPublicStationBySlug("khong-ton-tai"))
                .isInstanceOf(AppException.class)
                .extracting(ex -> ((AppException) ex).getErrorCode())
                .isEqualTo(ErrorCode.STATION_NOT_FOUND);
    }

    @Test
    void getPublicStationBySlug_hiddenStationThrowsStationNotFound() {
        MoodStation hidden = new MoodStation();
        ReflectionTestUtils.setField(hidden, "slug", "an-di");
        ReflectionTestUtils.setField(hidden, "isPublic", true);
        ReflectionTestUtils.setField(hidden, "isHiddenByAdmin", true);

        when(moodStationRepository.findBySlug("an-di")).thenReturn(Optional.of(hidden));

        assertThatThrownBy(() -> stationQueryService.getPublicStationBySlug("an-di"))
                .isInstanceOf(AppException.class)
                .extracting(ex -> ((AppException) ex).getErrorCode())
                .isEqualTo(ErrorCode.STATION_NOT_FOUND);
    }
}
