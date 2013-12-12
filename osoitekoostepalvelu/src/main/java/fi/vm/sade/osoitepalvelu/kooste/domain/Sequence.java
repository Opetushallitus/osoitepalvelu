package fi.vm.sade.osoitepalvelu.kooste.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/12/13
 * Time: 6:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Document(collection = "sequence")
public class Sequence implements Serializable {
    private String name;
    private long sequence;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }
}
