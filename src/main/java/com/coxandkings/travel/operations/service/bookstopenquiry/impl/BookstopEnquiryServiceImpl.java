package com.coxandkings.travel.operations.service.bookstopenquiry.impl;

import com.coxandkings.travel.operations.criteria.bookstopenquiry.BookstopEnquiryCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.bookstopenquiry.BookstopEnquiry;
import com.coxandkings.travel.operations.repository.bookstopenquiry.BookstopEnquiryRepository;
import com.coxandkings.travel.operations.resource.bookstopenquiry.BookstopEnquiryResource;
import com.coxandkings.travel.operations.service.bookstopenquiry.BookstopEnquiryService;
import com.coxandkings.travel.operations.utils.CopyUtils;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.coxandkings.travel.operations.utils.Constants.ER02;

@Transactional
@Service("bookStopEnquiryService")
public class BookstopEnquiryServiceImpl implements BookstopEnquiryService {

    private static final Logger logger = LogManager.getLogger(BookstopEnquiryServiceImpl.class);

    @Autowired
    private BookstopEnquiryRepository bookstopEnquiryRepository;

    private static Logger log = LogManager.getLogger(BookstopEnquiryServiceImpl.class);
    @Override
    public List<BookstopEnquiry> getByCriteria(BookstopEnquiryCriteria criteria) {
        return bookstopEnquiryRepository.getBookstopEnquiryByCriteria(criteria);
    }

    @Override
    public BookstopEnquiry getById(String id) {
        return bookstopEnquiryRepository.getBookstopEnquiryById(id);
    }

    @Override
    public BookstopEnquiry save(BookstopEnquiryResource bookstopEnquiryResource) throws OperationException {
        BookstopEnquiry bookstopEnquiry = null;
        bookstopEnquiry = new BookstopEnquiry();
        CopyUtils.copy(bookstopEnquiryResource,bookstopEnquiry);
        return bookstopEnquiryRepository.saveBookstopEnquiry(bookstopEnquiry);
    }

    @Override
    public BookstopEnquiry update(BookstopEnquiryResource bookstopEnquiryResource) throws OperationException {
        if (bookstopEnquiryResource.getId() == null) {
            logger.error((bookstopEnquiryResource.getId() + " id is not valid"));
            throw new OperationException(ER02, "please enter a valid primary key");
        }
        BookstopEnquiry existingBookstopEnquiry = bookstopEnquiryRepository.getBookstopEnquiryById(bookstopEnquiryResource.getId());
        CopyUtils.copy(bookstopEnquiryResource, existingBookstopEnquiry);
        return bookstopEnquiryRepository.updateBookstopEnquiry(existingBookstopEnquiry);
    }

//    @Override
//    public BookstopEnquiry saveAndUpdate(BookstopEnquiryResource bookstopEnquiryResource) throws OperationException {
//
//        BookstopEnquiry bookstopEnquiry = null;
//        try
//        {
//            if(!StringUtils.isEmpty(bookstopEnquiryResource.getId())) {
//                if(log.isDebugEnabled()) {
//                    log.debug("bookstopEnquiry Id:" + bookstopEnquiryResource.getId());
//                }
//
//                BookstopEnquiry existingBookstopEnquiry = bookstopEnquiryRepository.getBookstopEnquiryById(bookstopEnquiry.getId());
//
//                if(log.isDebugEnabled()) {
//                    log.debug("Existing bookstopEnquiry Details:" + existingBookstopEnquiry);
//                }
//                if(existingBookstopEnquiry == null) {
//                    throw new OperationException("BookstopEnquiry not found with id" + bookstopEnquiry.getId());
//                }
//
//                CopyUtils.copy(bookstopEnquiryResource, existingBookstopEnquiry);
//                bookstopEnquiry = existingBookstopEnquiry;
//
//            } else {
//                bookstopEnquiry = new BookstopEnquiry();
//                CopyUtils.copy(bookstopEnquiryResource,bookstopEnquiry);
//            }
//            return bookstopEnquiryRepository.saveOrUpdateBookstopEnquiry(bookstopEnquiry);
//        }catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public BookstopEnquiry updateBookstopEnquiryStatusToClosed(String id) {
        return bookstopEnquiryRepository.updateBookstopEnquiryStatusToClosed(id);
    }
}
