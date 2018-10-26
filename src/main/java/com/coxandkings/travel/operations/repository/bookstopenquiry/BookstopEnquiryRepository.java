package com.coxandkings.travel.operations.repository.bookstopenquiry;


import com.coxandkings.travel.operations.criteria.bookstopenquiry.BookstopEnquiryCriteria;
import com.coxandkings.travel.operations.model.bookstopenquiry.BookstopEnquiry;

import java.util.List;

public interface BookstopEnquiryRepository {

    BookstopEnquiry saveBookstopEnquiry(BookstopEnquiry bookstopEnquiry);
    BookstopEnquiry updateBookstopEnquiry(BookstopEnquiry bookstopEnquiry);
    List<BookstopEnquiry> getBookstopEnquiryByCriteria(BookstopEnquiryCriteria criteria);
    BookstopEnquiry getBookstopEnquiryById(String id);
    BookstopEnquiry updateBookstopEnquiryStatusToClosed(String id);

}
