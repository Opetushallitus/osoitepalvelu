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
        KOULUTA_KAYTTAJAT,
        AIPAL_KAYTTAJAT;
    };

    public enum TargetType {
        ORGANISAATIO,
        REHTORI,
        YHTEYSHENKILO,
        KRIISITIEDOTUS,
        KOULUTUSNEVONTA;
    }

    private GroupType group;
    private boolean selected;
    private List<TargetType> options = new ArrayList<TargetType>();

    public GroupType getGroup() {
        return group;
    }

    public void setGroup(GroupType group) {
        this.group = group;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<TargetType> getOptions() {
        return options;
    }

    public void setOptions(List<TargetType> options) {
        this.options = options;
    }
}
