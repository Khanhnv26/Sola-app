package com.sola.common.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageResponse <T>{
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private T content;
    private boolean first;
    private boolean last;

    public static <T> PageResponse<T> from (Page<?> page, T content) {
        return PageResponse.<T>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(content)
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
