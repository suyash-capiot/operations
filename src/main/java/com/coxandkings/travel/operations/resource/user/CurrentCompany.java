
package com.coxandkings.travel.operations.resource.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_id",
    "name",
    "goc",
    "gc"
})
public class CurrentCompany implements Serializable
{

    @JsonProperty("_id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("goc")
    private Goc goc;
    @JsonProperty("gc")
    private Gc gc;
    private final static long serialVersionUID = -1193665725406117521L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CurrentCompany() {
    }

    /**
     * 
     * @param name
     * @param goc
     * @param id
     * @param gc
     */
    public CurrentCompany(String id, String name, Goc goc, Gc gc) {
        super();
        this.id = id;
        this.name = name;
        this.goc = goc;
        this.gc = gc;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("goc")
    public Goc getGoc() {
        return goc;
    }

    @JsonProperty("goc")
    public void setGoc(Goc goc) {
        this.goc = goc;
    }

    @JsonProperty("gc")
    public Gc getGc() {
        return gc;
    }

    @JsonProperty("gc")
    public void setGc(Gc gc) {
        this.gc = gc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CurrentCompany.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("goc");
        sb.append('=');
        sb.append(((this.goc == null)?"<null>":this.goc));
        sb.append(',');
        sb.append("gc");
        sb.append('=');
        sb.append(((this.gc == null)?"<null>":this.gc));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.goc == null)? 0 :this.goc.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.gc == null)? 0 :this.gc.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CurrentCompany) == false) {
            return false;
        }
        CurrentCompany rhs = ((CurrentCompany) other);
        return (((((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name)))&&((this.goc == rhs.goc)||((this.goc!= null)&&this.goc.equals(rhs.goc))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.gc == rhs.gc)||((this.gc!= null)&&this.gc.equals(rhs.gc))));
    }

}
