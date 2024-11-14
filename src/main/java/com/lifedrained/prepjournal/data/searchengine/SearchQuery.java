package com.lifedrained.prepjournal.data.searchengine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SearchQuery<T extends Enum<T>> {
    private Enum<T> type;
    private Object value;

}
