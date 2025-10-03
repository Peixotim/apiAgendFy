package marketing.digital.rj.apiagendfy.scheduling.dto;


public record SlotDTO(
        String startAt,
        String endAt,
        int capacity,
        int booked,
        String collaboratorId,
        String collaboratorName,
        String collaboratorPhotoUrl
) {}