package ru.practicum.events.mapper;

import ru.practicum.events.dto.AdminSearchDto;
import ru.practicum.events.dto.UserSearchDto;
import ru.practicum.events.model.Sort;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParamMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static AdminSearchDto toAdminSearch(List<Long> users, List<State> states, List<Long> categories,
                                               String rangeStart, String rangeEnd) {
        AdminSearchDto param = new AdminSearchDto();
        param.setUsers(users);
        param.setStates(states);
        param.setCategories(categories);
        if (rangeStart != null) {
            param.setRangeStart(LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER));
        }
        if (rangeEnd != null) {
            param.setRangeEnd(LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER));
        }
        return param;
    }

    public static UserSearchDto toUserSearch(String text, List<Long> categories, Boolean paid, String rangeStart,
                                             String rangeEnd, Boolean onlyAvailable, String sort) {
        UserSearchDto param = new UserSearchDto();
        if (text != null) {
            param.setText(text.toLowerCase());
        }
        if (categories != null) {
            param.setCategories(categories);
        }
        if (paid != null) {
            param.setPaid(paid);
        }
        if (rangeStart != null) {
            param.setRangeStart(LocalDateTime.parse(rangeStart, DATE_TIME_FORMATTER));
        }
        if (rangeEnd != null) {
            param.setRangeEnd(LocalDateTime.parse(rangeEnd, DATE_TIME_FORMATTER));
            param.setOnlyAvailable(onlyAvailable);
            param.setSort(Sort.valueOf(sort));
        }
        return param;
    }
}
