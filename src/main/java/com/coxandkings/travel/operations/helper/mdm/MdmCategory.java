package com.coxandkings.travel.operations.helper.mdm;

public class MdmCategory {
    private String _id;
    private String ancillaryType;
    private Boolean deleted;
    private Integer __v;
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAncillaryType() {
        return ancillaryType;
    }

    public void setAncillaryType(String ancillaryType) {
        this.ancillaryType = ancillaryType;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }
}
