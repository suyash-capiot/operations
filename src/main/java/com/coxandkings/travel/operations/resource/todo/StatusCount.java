package com.coxandkings.travel.operations.resource.todo;

public class StatusCount {
    private String toDoStatus;
    private Long count;

    public StatusCount() {
    }

    public StatusCount(String toDoStatus, Long count) {
        this.toDoStatus = toDoStatus;
        this.count = count;
    }

    public String getToDoStatus() {
        return toDoStatus;
    }

    public void setToDoStatus(String toDoStatus) {
        this.toDoStatus = toDoStatus;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object toObject) {
        boolean isEqual = false;
        if( toObject instanceof StatusCount )    {
            StatusCount anotherObj = (StatusCount) toObject;
            if( toDoStatus != null && anotherObj.getToDoStatus() != null )  {
                if( toDoStatus.equalsIgnoreCase( anotherObj.getToDoStatus() ))  {
                    isEqual = true;
                }
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int aHashCode = -1;
        if( toDoStatus == null )    {
            aHashCode = super.hashCode();
        }
        else {
            aHashCode = toDoStatus.hashCode();
        }

        return aHashCode;
    }
}
