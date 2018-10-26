package com.coxandkings.travel.operations.service.bookstopenquiry;


import com.coxandkings.travel.operations.criteria.bookstopenquiry.BookstopEnquiryCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.bookstopenquiry.BookstopEnquiry;
import com.coxandkings.travel.operations.resource.bookstopenquiry.BookstopEnquiryResource;

import java.util.List;

public interface BookstopEnquiryService {

    List<BookstopEnquiry> getByCriteria(BookstopEnquiryCriteria criteria);
    BookstopEnquiry getById(String id);
    BookstopEnquiry save(BookstopEnquiryResource bookstopEnquiryResource) throws OperationException;
    BookstopEnquiry update(BookstopEnquiryResource bookstopEnquiryResource) throws OperationException;
    BookstopEnquiry updateBookstopEnquiryStatusToClosed(String id);
}
