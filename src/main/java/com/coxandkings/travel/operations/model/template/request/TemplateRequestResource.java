
package com.coxandkings.travel.operations.model.template.request;

import com.coxandkings.travel.operations.resource.email.DynamicVariables;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lookup",
    "commands"
})
public class TemplateRequestResource {

    @JsonProperty("lookup")
    private String lookup;
    @JsonProperty("commands")
    private List<Command> command;// = null;

    public TemplateRequestResource(TemplateInfo templateInfo, List <DynamicVariables> dynamicVariables, String lookUpValue, String outIdentifier, Boolean entryPoint, String returnObject, String userId, String transactionId) {
        this.lookup = lookUpValue;
        Command insert;
        Command fireAllRules;
        Command getForObjects;
        insert = new Command();
        insert.setInsert(new Insert(templateInfo, dynamicVariables, outIdentifier, entryPoint, returnObject, userId, transactionId));
        fireAllRules = new Command();
        fireAllRules.setFireAllRules(new FireAllRules());
        getForObjects = new Command();
        getForObjects.setGetObjects(new GetObjects());
        this.command = new ArrayList<>();
        this.command.add(insert);
        this.command.add(fireAllRules);
        this.command.add(getForObjects);

    }

    @JsonProperty("lookup")
    public String getLookup() {
        return lookup;
    }

    @JsonProperty("lookup")
    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public List<Command> getCommand() {
        return command;
    }

    public void setCommand(List<Command> command) {
        this.command = command;
    }
}
