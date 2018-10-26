package com.coxandkings.travel.operations.service.prodreview;

import com.coxandkings.travel.operations.exceptions.OperationException;
import org.json.JSONException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public interface ProductReviewSchedulerService {

    void sendReviewToClient() throws ParseException, OperationException, MessagingException, IOException, SQLException, JSONException;

    /*void updateAging();*/
}
