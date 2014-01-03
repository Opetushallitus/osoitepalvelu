package fi.vm.sade.osoitepalvelu.kooste.service.kooste.dto;

public enum KoodistoTila {
    PASSIIVINEN, 
    LUONNOS, 
    HYVAKSYTTY;

    public static boolean isAktiivinenTila(KoodistoTila tila) {
        return (tila != null && (tila == KoodistoTila.LUONNOS || tila == KoodistoTila.HYVAKSYTTY));
    }
}
