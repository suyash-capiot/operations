package com.coxandkings.travel.operations.controller.FileProfitability;

import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchCriteria;
import com.coxandkings.travel.operations.criteria.fileprofitability.FileProfSearchReportCriteria;
import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.model.booking.KafkaBookingMessage;
import com.coxandkings.travel.operations.model.modifiedFileProfitabiliy.FileProfitabilityBooking;
import com.coxandkings.travel.operations.repository.fileProfitabilityModified.FileProfitabilityModifiedRepository;
import com.coxandkings.travel.operations.service.FileProfitability.FileProfitabilityService;
import com.coxandkings.travel.operations.service.FileProfitability.impl.FileProfServiceImplSplit;
import com.coxandkings.travel.operations.service.FileProfitability.impl.FileProfitabilityServiceImpl;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file-profitability")
@CrossOrigin(origins = "*")
public class FileProfitabilityController {
    private static final Logger logger = Logger.getLogger(FileProfitabilityController.class);
    @Autowired
    FileProfitabilityService fileProfitabilityService;
    @Autowired
    FileProfitabilityServiceImpl fileProfitabilityServiceImpl;
    @Autowired
    FileProfitabilityModifiedRepository fileProfModifiedRepo;
    @Autowired
    FileProfServiceImplSplit fileProfServiceImplSplit;

    //Testing endpoint
    @GetMapping(value = "/Execute")
    public ResponseEntity<Object> find() throws OperationException {
        try {
            KafkaBookingMessage kafkaBookingMessage = new KafkaBookingMessage();
            fileProfModifiedRepo.updateAllDBRecs();
            String a = "success";
            return new ResponseEntity<>(a, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/test")
    public ResponseEntity<String> test() throws OperationException {

        KafkaBookingMessage kafkaBookingMessage = new KafkaBookingMessage();
        fileProfitabilityServiceImpl.opsBookingFromKafka(kafkaBookingMessage);

        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @PostMapping(value = "/v1/getFileProfsByCriteria")
    public ResponseEntity<?> findFileProfBySeachCriteria(@RequestBody FileProfSearchCriteria bookingCriteria) throws OperationException {
        try {
            logger.info("in findFileProfBySeachCriteria() method From FileProfitabilityController");
            List<FileProfitabilityBooking> generic = new ArrayList<>();
            List<FileProfitabilityBooking> fileProfTransPo = null;
            List<FileProfitabilityBooking> fileProfAccomo = null;
            List<FileProfitabilityBooking> randomEntry = null;
            HashMap<String, Object> result = new HashMap<>();

            if (bookingCriteria.getIsBookingRefNumbWise()) {
                bookingCriteria.setIsTransportation(true);
                fileProfTransPo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
                bookingCriteria.setIsTransportation(false);
                bookingCriteria.setIsAccomodation(true);
                fileProfAccomo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
            } else {
                bookingCriteria.setIsTransportation(false);
                bookingCriteria.setIsAccomodation(false);
                bookingCriteria.setIsPassengerwise(true);
                fileProfTransPo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
                bookingCriteria.setIsPassengerwise(false);
                bookingCriteria.setIsRoomwise(true);
                fileProfAccomo = fileProfitabilityServiceImpl.getListOfFileProfsWRTCriteria(bookingCriteria);
            }
//        }
            if (fileProfAccomo != null)
                generic.addAll(fileProfAccomo);
            if (fileProfTransPo != null)
                generic.addAll(fileProfTransPo);
            if (randomEntry != null)
                generic.addAll(randomEntry);
            result = fileProfitabilityServiceImpl.applyPagination(generic, bookingCriteria.getPageNumber(), bookingCriteria.getPageSize());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/v1/getFileProfsBetweenDates")
    public ResponseEntity<?> getFileProfsBetweenDates(@RequestBody FileProfSearchCriteria bookingCriteria) {
        try {
            logger.info("in getFileProfsBetweenDates() method From FileProfitabilityController");

            List<FileProfitabilityBooking> generic = new ArrayList<>();
            List<FileProfitabilityBooking> fileProfTransPo = null;
            List<FileProfitabilityBooking> fileProfAccomo = null;

            Set<String> bookRefNumbs = new HashSet<>();
            Set<String> productNames = new HashSet<>();
            Set<String> leadPassNames = new HashSet<>();

            fileProfTransPo = fileProfModifiedRepo.getAutoSuggest(bookingCriteria);
            generic.addAll(fileProfTransPo);

            for (FileProfitabilityBooking fileProfitabilityBooking : generic) {
                bookRefNumbs.add(fileProfitabilityBooking.getBookingReferenceNumber());
                productNames.add(fileProfitabilityBooking.getProductName());
                leadPassNames.add(fileProfitabilityBooking.getLeadPassName());
            }

            JSONArray jsonArray = new JSONArray();
            if (bookingCriteria.isBookingRefNumbWise()) { //if its booking ref auto suggest
                bookRefNumbs = generic.stream().map(x -> x.getBookingReferenceNumber()).collect(Collectors.toSet());
                bookRefNumbs.remove(null);

                Iterator<String> bookRefIterator = bookRefNumbs.iterator();

                while (bookRefIterator.hasNext()) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("bookingRefNumber", bookRefIterator.next());
                    jsonArray.put(jsonObject);
                }

            }
            else // if it is a pax auto suggest
            {
                leadPassNames = generic.stream().map(x -> x.getLeadPassName()).collect(Collectors.toSet());
                leadPassNames.remove(null);

                Iterator<String> leaderPassItr = leadPassNames.iterator();

                while (leaderPassItr.hasNext()) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("leadPassengerName", leaderPassItr.next());
                    jsonArray.put(jsonObject);
                }
            }

            return new ResponseEntity<>(jsonArray, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/v1/getbybookid")
    public ResponseEntity<?> getAllFPsBookingWise(@RequestBody FileProfSearchCriteria bookingCriteria) {
        HashMap<String, Object> result = new HashMap<>();
        List<FileProfitabilityBooking> fls = fileProfServiceImplSplit.getFileProfsWRTBookingId(bookingCriteria);

        return new ResponseEntity<>(fls, HttpStatus.OK);
    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/v1/export/type/{exportType}")
    public ResponseEntity<byte[]> exportReport(@PathVariable("exportType") String exportType, @RequestBody FileProfSearchReportCriteria bookingCriteria) throws OperationException, IOException, RepositoryException {
        MultipartFile file = fileProfServiceImplSplit.exportReport(bookingCriteria, exportType);
        byte[] response = file.getBytes();


        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        if (exportType.equalsIgnoreCase("pdf")) {
            headers.set("Content-Type", "application/pdf");
        } else {
            headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }


        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}