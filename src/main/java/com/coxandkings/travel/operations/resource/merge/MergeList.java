package com.coxandkings.travel.operations.resource.merge;

import java.util.List;

public class MergeList {
 
	private List<? extends MergeResource> withinCancellationPeriod;
	private List<? extends MergeResource> outsideCancellationPeriod;
	
	public List<? extends MergeResource> getOutsideCancellationPeriod() {
		return outsideCancellationPeriod;
	}
	public void setOutsideCancellationPeriod(List<? extends MergeResource> outsideCancellationPeriod) {
		this.outsideCancellationPeriod = outsideCancellationPeriod;
	}
	
	public List<? extends MergeResource> getWithinCancellationPeriod() {
		return withinCancellationPeriod;
	}
	public void setWithinCancellationPeriod(List<? extends MergeResource> withinCancellationPeriod) {
		this.withinCancellationPeriod = withinCancellationPeriod;
	}
	
	public MergeList(List<? extends MergeResource> withinCancellationPeriod,
			List<? extends MergeResource> outsideCancellationPeriod) {
		super();
		this.withinCancellationPeriod = withinCancellationPeriod;
		this.outsideCancellationPeriod = outsideCancellationPeriod;
	}
	public MergeList() {
		super();
	}
	
}
