package pl.HomeworkJustClick.Backend.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notification")
public class NotificationController {
    private final NotificationService service;

    @GetMapping("byUser/{userId}")
    @Operation(
            summary = "Returns paged list of notifications by userId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "20", description = "default = 10"),
                    @Parameter(name = "sort", example = "id,asc", description = "default = date,desc")
            }
    )
    public Slice<NotificationResponseDto> getNotificationsByUserId(@PathVariable Integer userId, @Parameter(hidden = true) @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.getNotificationsByUserId(userId, pageable);
    }

    @GetMapping("countByUser/{userId}")
    @Operation(
            summary = "Returns counter of notifications by userId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Integer.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public Integer countNotificationsByUserId(@PathVariable Integer userId) {
        return service.countNotificationsByUserId(userId);
    }

    @PostMapping
    @Operation(
            summary = "Updates read parameter to true by notifications ids list. ex. /api/notification/notificationsIds=1,2,3",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotificationResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Notification not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public List<NotificationResponseDto> updateReadInNotifications(@RequestParam List<Integer> notificationsIds) {
        return service.updateReadInNotifications(notificationsIds);
    }

    @DeleteMapping("{notificationId}")
    @Operation(
            summary = "Deletes notification by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Notification not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteNotification(@PathVariable Integer notificationId) {
        service.deleteNotification(notificationId);
    }
}
