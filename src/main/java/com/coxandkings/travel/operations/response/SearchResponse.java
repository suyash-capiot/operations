package com.coxandkings.travel.operations.response;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse
{
    private List<?> contents = new ArrayList<>();
    private long totalElements;
    private boolean last;
    private boolean first;
    private int totalPages;
    private int size;
    private int page;
    private long numberOfElements;
}
