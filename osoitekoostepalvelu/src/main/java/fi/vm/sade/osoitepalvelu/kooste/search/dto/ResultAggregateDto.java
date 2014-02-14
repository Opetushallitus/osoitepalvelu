package fi.vm.sade.osoitepalvelu.kooste.search.dto;

import fi.vm.sade.osoitepalvelu.kooste.common.util.EqualsHelper;
import fi.vm.sade.osoitepalvelu.kooste.search.api.KayttajahakuResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OrganisaatioResultDto;
import fi.vm.sade.osoitepalvelu.kooste.search.api.OsoitteistoDto;

/**
 * Created with IntelliJ IDEA.
 * User: ratamaa
 * Date: 2/14/14
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultAggregateDto {
    private OrganisaatioResultDto organisaatio;
    private KayttajahakuResultDto henkilo;
    private OsoitteistoDto osoite;

    public ResultAggregateDto(OrganisaatioResultDto organisaatio, KayttajahakuResultDto henkilo, OsoitteistoDto osoite) {
        this.organisaatio = organisaatio;
        this.henkilo = henkilo;
        this.osoite = osoite;
    }

    public OrganisaatioResultDto getOrganisaatio() {
        return organisaatio;
    }

    public KayttajahakuResultDto getHenkilo() {
        return henkilo;
    }

    public OsoitteistoDto getOsoite() {
        return osoite;
    }

    @Override
    public int hashCode() {
        int result = organisaatio.getOid().hashCode();
        result = 31 * result + (henkilo != null ? henkilo.getOid().hashCode() : 0);
        result = 31 * result + (osoite != null ? osoite.getYhteystietoOid().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultAggregateDto that = (ResultAggregateDto) o;
        if (!EqualsHelper.equals(organisaatio.getOid(), that.organisaatio)) {
            return false;
        }
        if (EqualsHelper.differentNulls(osoite, that.osoite)
                || (EqualsHelper.notNulls(osoite, that.osoite)
                && !EqualsHelper.equals(henkilo.getOid(), that.henkilo.getOid()))) {
            return false;
        }
        if ( EqualsHelper.differentNulls(osoite, that.osoite)
                || (EqualsHelper.notNulls(osoite, that.osoite)
                        && !EqualsHelper.equals(osoite.getYhteystietoOid(), that.osoite.getYhteystietoOid())) ) {
            return false;
        }
        return true;
    }
}
