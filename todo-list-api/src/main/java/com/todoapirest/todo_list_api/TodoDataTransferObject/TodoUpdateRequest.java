package com.todoapirest.todo_list_api.TodoDataTransferObject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequest(
        @NotNull(message = "Id is required")
        @Min(value = 1, message = "ID must be greater than 0")
        long id,

        @NotNull(message = "Title is required")
        @Size(min = 3, max = 60, message = "Title must be between 3 and 60 characters")
        String title,

        @NotNull(message = "Completed field is required")
        boolean completed,

        @Size( max = 400, message = "Description must be 400 characters maximum")
        String description,

        @Min(value = 1, message = "Priority field must be between 1 and 5")
        @Max(value = 5, message = "Priority field must be between 1 and 5")
        int priority
) {}
