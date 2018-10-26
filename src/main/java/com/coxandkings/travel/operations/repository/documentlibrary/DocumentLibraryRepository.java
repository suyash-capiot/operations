package com.coxandkings.travel.operations.repository.documentlibrary;

import com.coxandkings.travel.operations.model.documentlibrary.NewDocument;

public interface DocumentLibraryRepository {

	NewDocument getById(String id);

	NewDocument saveOrUpdate(NewDocument document);

	NewDocument update(NewDocument document);
	
	NewDocument getByCriteria(NewDocument document);
    
}
