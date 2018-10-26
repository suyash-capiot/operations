package com.coxandkings.travel.operations.controller.prodreview;


import com.coxandkings.travel.operations.criteria.prodreview.ProductAverageCriteria;
import com.coxandkings.travel.operations.model.prodreview.ProductReviewAverage;
import com.coxandkings.travel.operations.service.prodreview.ProductReviewAverageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productReviewAverage")
@CrossOrigin(origins = "*")
public class ProductReviewAverageController {

    @Autowired
    private ProductReviewAverageService productReviewAverageService;

    @PostMapping("/v1/getByCriteria")
    private List<ProductReviewAverage> getReviewAverage(@RequestBody ProductAverageCriteria productAverageCriteria) {
        return productReviewAverageService.getByCriteria(productAverageCriteria);
    }

}
