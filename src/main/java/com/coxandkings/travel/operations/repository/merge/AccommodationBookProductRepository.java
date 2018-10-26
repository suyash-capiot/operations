package com.coxandkings.travel.operations.repository.merge;

import com.coxandkings.travel.operations.model.merge.AccommodationBookProduct;

public interface AccommodationBookProductRepository {
    AccommodationBookProduct saveOrUpdate(AccommodationBookProduct accommodationBookProduct);

    AccommodationBookProduct getById(String id);
}
