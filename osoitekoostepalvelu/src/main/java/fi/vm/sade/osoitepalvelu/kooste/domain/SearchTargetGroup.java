package fi.vm.sade.osoitepalvelu.kooste.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 12/10/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchTargetGroup implements Serializable {
    public enum GroupType {
        JARJESTAJAT_YLLAPITAJAT,
        OPPILAITOKSET,
        OPETUSPISTEET,
        OPPISOPIMUSTOIMPISTEET,
        MUUT_ORGANISAATIOT,
        TUTKINTOTOIMIKUNNAT,
        KOULUTA_KAYTTAJAT,
        AIPAL_KAYTTAJAT;
    };

    public enum TargetType {
        ORGANISAATIO,
        REHTORI,
        YHTEYSHENKILO,
        KRIISITIEDOTUS,
        KOULUTUSNEVONTA,
        PUHEENJOHTAJA,
        SIHTEERI,
        JASENET,
        TUNNUKSENHALTIJAT;
    };

    private GroupType type;
    private List<TargetType> options = new ArrayList<TargetType>();

    public SearchTargetGroup() {
    }

    public SearchTargetGroup(GroupType type, List<TargetType> options) {
        this.type = type;
        this.options = options;
    }

    public GroupType getType() {
        return type;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public List<TargetType> getOptions() {
        return options;
    }

    public void setOptions(List<TargetType> options) {
        this.options = options;
    }
}
