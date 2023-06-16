package ru.practicum.events.mapper;

import ru.practicum.events.dto.AdminSearchDto;
import ru.practicum.events.dto.UserSearchDto;
import ru.practicum.events.model.Sort;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParamMapper {

    public static AdminSearchDto toAdminSearch(List<Long> users, List<State> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        AdminSearchDto param = new AdminSearchDto();
        param.setUsers(users);
        param.setStates(states);
        param.setCategories(categories);
        if (rangeStart != null) {
            param.setRangeStart(rangeStart);
        }
        if (rangeEnd != null) {
            param.setRangeEnd(rangeEnd);
        }
        return param;
    }

    public static UserSearchDto toUserSearch(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Boolean onlyAvailable, String sort) {
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
            param.setRangeStart(rangeStart);
        }
        if (rangeEnd != null) {
            param.setRangeEnd(rangeEnd);
            param.setOnlyAvailable(onlyAvailable);
            param.setSort(Sort.valueOf(sort));
        }
        return param;
    }
}
