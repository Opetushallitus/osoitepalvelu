package fi.vm.sade.osoitepalvelu.kooste.domain;

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Document(collection = "savedSearches")
public class SavedSearch implements Serializable, Comparable<SavedSearch> {
    public enum SaveType {
        EMAIL,
        LETTER,
        CONTACT;
    }

    @Id
    private Long id;
    private String name;
    private String ownerUsername;
    private SaveType type;
    private DateTime createdAt=new DateTime();
    private List<String> addressFields = new ArrayList<String>();
    private List<SearchTargetGroup> targetGroups = new ArrayList<SearchTargetGroup>();
    private List<SearchTerm> terms = new ArrayList<SearchTerm>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public SaveType getType() {
        return type;
    }

    public void setType(SaveType type) {
        this.type = type;
    }

    public List<String> getAddressFields() {
        return addressFields;
    }

    public void setAddressFields(List<String> addressFields) {
        this.addressFields = addressFields;
    }

    public List<SearchTargetGroup> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<SearchTargetGroup> targetGroups) {
        this.targetGroups = targetGroups;
    }

    public List<SearchTerm> getTerms() {
        return terms;
    }

    public void setTerms(List<SearchTerm> terms) {
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int compareTo(SavedSearch o) {
        return this.getCreatedAt().compareTo(o.getCreatedAt());
    }
}
