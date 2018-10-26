package com.coxandkings.travel.operations.schedular;

import com.coxandkings.travel.operations.service.merge.MergeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class MergeJob implements Job {
	@Autowired
	MergeService mergeService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Merge job is executing...");
        
//        try {
//            System.out.println(mergeService.splitMerge(jobDataMap.getString("mergeId"), jobDataMap.getString("supplierId")));
//        } catch (OperationException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        System.out.println(mergeService.toString());
    }
}
