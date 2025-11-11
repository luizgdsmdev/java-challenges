package com.todoapirest.todo_list_api.Exceptions.Records;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String details,
        LocalDateTime timestamp
) {}
