package com.coxandkings.travel.operations.security;

import com.coxandkings.travel.operations.exceptions.OperationException;
import com.coxandkings.travel.operations.resource.user.MdmUserInfo;
import com.coxandkings.travel.operations.resource.user.OpsUser;
import com.coxandkings.travel.operations.service.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class OpsRequestFilter extends OncePerRequestFilter {

    @Value("${operation.security.tokenHeader}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    private static Logger log = LogManager.getLogger(OpsRequestFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtToken = request.getHeader(this.tokenHeader);
        String authToken = null;

        OpsUser user = null;
/*        if (new AntPathRequestMatcher("/operations/").matches(request) || new AntPathRequestMatcher("/bookingService/**").matches(request) ||
                new AntPathRequestMatcher("/swagger-ui.html/**").matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }*/

        logger.info("You hit " + request.getRequestURL() + " URL");
        //*** WORKING SUDHIR CODE ***
        if (jwtToken == null || jwtToken.trim().length() == 0) {
            if (new AntPathRequestMatcher("/operations/**").matches(request) ||
                    new AntPathRequestMatcher("/bookingService/**").matches(request) ||
                    new AntPathRequestMatcher("/documentLibrary/v1/get/**").matches(request) ||
                    new AntPathRequestMatcher("/documentLibrary/v2/get/**").matches(request) ||
                    new AntPathRequestMatcher("/swagger-ui.html/**").matches(request) ||
                    new AntPathRequestMatcher("/webjars/**").matches(request) ||
                    new AntPathRequestMatcher("/v2/api-docs/**").matches(request) ||
                    new AntPathRequestMatcher("/configuration/ui/**").matches(request) ||
                    new AntPathRequestMatcher("/configuration/security/**").matches(request) ||
                    new AntPathRequestMatcher("/finance/**").matches(request) ||
                    new AntPathRequestMatcher("/testing/**").matches(request) ||
                    new AntPathRequestMatcher("/zmock/**").matches(request) ||
                    new AntPathRequestMatcher("/supplierConfigurations/**").matches(request) ||
                    new AntPathRequestMatcher("/zmock/**").matches(request) ||
                    new AntPathRequestMatcher("/commercials/**").matches(request) ||
                    new AntPathRequestMatcher("/supplierInvoice/**").matches(request) ||
                    new AntPathRequestMatcher("/bookingPrices/**").matches(request) ||
                    new AntPathRequestMatcher("/products/**").matches(request) ||
                    new AntPathRequestMatcher("**/mdm/**").matches(request) ||
                    new AntPathRequestMatcher("/client/customer/**").matches(request) ||
                    new AntPathRequestMatcher("/supplier/service-provider/**").matches(request) ||
                    //new AntPathRequestMatcher("/documentLibrary/**").matches(request) ||
                    new AntPathRequestMatcher("/productReviews/**").matches(request) ||
                    new AntPathRequestMatcher("/productReviewTemplate/**").matches(request) ||
                    new AntPathRequestMatcher("/swagger-resources/**").matches(request) ||
                    new AntPathRequestMatcher("/supplier/service-provider/**").matches(request) ||
                    new AntPathRequestMatcher("/supplierResponse/operations/reconfirmation/supplierResponse/**").matches(request) ||
                    new AntPathRequestMatcher("/clientResponse/operations/reconfirmation/clientResponse/**").matches(request) ||
                    new AntPathRequestMatcher("/clientResponse/operations/productSharing/clientResponse/**").matches(request) ||
                    new AntPathRequestMatcher("/beCommercial/**").matches(request) ||
                    new AntPathRequestMatcher("/productSharing/**").matches(request) ||
                    new AntPathRequestMatcher("/reinstate/supplierResponse/**").matches(request) ||
                    new AntPathRequestMatcher("/amendments/supplierResponse/**").matches(request) ||
                    new AntPathRequestMatcher("/fullCancellations/v1/updateOrderStatusNotExternetSupplier/**").matches(request) ||
                    new AntPathRequestMatcher("/noShow/**").matches(request) ||
                    new AntPathRequestMatcher("/products/bookedThroughOtherSevices/v1/updatedByClient").matches(request) ||
                    new AntPathRequestMatcher("/products/bookedThroughOtherSevices/v1/productSubType/{productCategorySubTypeValue}/id/{id}").matches(request) ||
                    new AntPathRequestMatcher("/drivers/v1/updateBySupplier").matches(request) ||
                    new AntPathRequestMatcher("/changeSupplierName/v1/client/**").matches(request) ||
                    new AntPathRequestMatcher("/documentLibrary/v1/htmlToPDF/**").matches(request) ||
                    new AntPathRequestMatcher("/drivers/v1/id/{id}").matches(request) ||
					new AntPathRequestMatcher("/alternateOptions/v1/update/{id}").matches(request) ||
                    new AntPathRequestMatcher("/mailrooms/v1/update").matches(request) ||
                    new AntPathRequestMatcher("/mailrooms/v1/searchById/{id}").matches(request))

            {

                filterChain.doFilter(request, response);
                return;
            }

        }

        MdmUserInfo mdmUserInfo = null;
        try {
            mdmUserInfo = userService.assertToken(jwtToken);
            if (mdmUserInfo == null) {
                filterChain.doFilter(request, response);
                return;
            }
            user = userService.getOpsUser(mdmUserInfo, jwtToken);
            user.setUserID(mdmUserInfo.getUser().getId());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            log.info("authenticated user " + user.getUsername() + ", setting security context");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (OperationException e) {
            logger.error("Unable to authentication user token - token might have expired!");
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED Request");

        }

    }
}
