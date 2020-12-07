package fi.vm.sade.osoitepalvelu.kooste.service.search;

import com.google.common.base.Optional;
import fi.vm.sade.javautils.poi.OphCellStyles;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultOsoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsPresentationDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.common.util.StringHelper.join;


@Service
public class SearchExcelServiceImpl extends AbstractService implements SearchExcelService {


    @Autowired
    private MessageService messageService;

    @Override
    public void produceExcel(Workbook workbook, SearchResultsPresentationDto searchResults) {
        Sheet sheet = workbook.createSheet();

        int rowNum = 0;
        int maxColumn = produceHeader(sheet, rowNum, 0, searchResults.getPresentation());
        OphCellStyles ophCellStyles = new OphCellStyles(workbook);
        for (SearchResultRowDto row : searchResults.getRows()) {
            produceRow(searchResults.getPresentation(), sheet, ++rowNum, row, ophCellStyles);
        }
        for (int i = 0; i < maxColumn; ++i) {
            sheet.autoSizeColumn(i);
        }
    }

    protected int produceHeader(Sheet sheet, int rowNum, int cellNum, SearchResultPresentation presentation) {
        if (presentation.isOrganisaationNimiIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaatio_nimi");
        }
        if (presentation.isOrganisaatioOidIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaatio_oid");
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaation_tunniste");
        }
        if (presentation.isYtunnusIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_ytunnus");
        }
        if (presentation.isYritysmuotoIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_yritysmuoto");
        }
        if (presentation.isOpetuskieliIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_opetuskieli");
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_yhteyshenkilo");
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_yhteyshenkilo_email");
        }
        if (presentation.isOrganisaatioEmailIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaatio_email");
        }
        if (presentation.isPositosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite");
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite_postinumero");
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_postiosoite_postitoimipaikka");
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_puhelinnumero");
        }
        if (presentation.isWwwOsoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_www_osoite");
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_viranomaistiedotuksen_email");
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_koulutusneuvonnan_email");
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_kriisitiedotuksen_email");
        }
        if (presentation.isVarhaiskasvatuksenYhteyshenkiloIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_varhaiskasvatuksen_yhteyshenkilo");
        }
        if (presentation.isVarhaiskasvatuksenEmailIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_varhaiskasvatuksen_email");
        }
        if(presentation.isKoskiYhdyshenkiloIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_koski_yhdyshenkilo");
        }
        if (presentation.isMoveYhteyshenkiloIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_move_yhteyshenkilo");
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaation_sijaintikunta");
        }
        return cellNum - 1;
    }

    private String osoite(SearchResultOsoiteDto osoite) {
        if (osoite == null) {
            return "";
        }
        return join(" ",
                osoite.getOsoite(),
                osoite.getExtraRivi()//,
                //join(" ", osoite.getPostinumero(), osoite.getPostitoimipaikka())
        );
    }

    private void produceRow(SearchResultPresentation presentation, Sheet sheet, int rowNum, SearchResultRowDto row, OphCellStyles ophCellStyles) {
        int cellNum = 0;
        if (presentation.isOrganisaationNimiIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getNimi(), ophCellStyles);
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getOppilaitosKoodi(), ophCellStyles);
        }
        if (presentation.isOrganisaatioOidIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getOrganisaatioOid(), ophCellStyles);
        }
        if (presentation.isYtunnusIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getYtunnus(), ophCellStyles);
        }
        if (presentation.isYritysmuotoIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getYritysmuoto(), ophCellStyles);
        }
        if (presentation.isOpetuskieliIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getOpetuskieli(), ophCellStyles);
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), join(" ", row.getYhteystietoNimi()), ophCellStyles);
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getHenkiloEmail(), ophCellStyles);
        }
        if (presentation.isOrganisaatioEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getEmailOsoite(), ophCellStyles);
        }
        if (presentation.isPositosoiteIncluded()) {
            SearchResultOsoiteDto osoite = row.getPostiosoite();
            value(cell(sheet, rowNum, cellNum++), osoite(osoite), ophCellStyles);
            String postinumero = null,
                    postitoimipaikka = null;
            if (osoite != null) {
                postinumero = osoite.getPostinumero();
                postitoimipaikka = osoite.getPostitoimipaikka();
            }
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postinumero).or(""), ophCellStyles);
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postitoimipaikka).or(""), ophCellStyles);
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getPuhelinnumero(), ophCellStyles);
        }
        if (presentation.isWwwOsoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getWwwOsoite(), ophCellStyles);
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getViranomaistiedotuksenEmail(), ophCellStyles);
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKoulutusneuvonnanEmail(), ophCellStyles);
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKriisitiedotuksenEmail(), ophCellStyles);
        }
        if(presentation.isVarhaiskasvatuksenYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getVarhaiskasvatuksenYhteyshenkilo(), ophCellStyles);
        }
        if(presentation.isVarhaiskasvatuksenEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getVarhaiskasvatuksenEmail(), ophCellStyles);
        }
        if(presentation.isKoskiYhdyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKoskiYhdyshenkilo(), ophCellStyles);
        }
        if (presentation.isMoveYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getMoveYhteyshenkilo(), ophCellStyles);
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKotikunta(), ophCellStyles);
        }

    }

    protected Cell value(Cell cell, String value, OphCellStyles ophCellStyles) {
        if (value != null) {
            cell.setCellValue(value);
        }
        ophCellStyles.apply(cell);
        return cell;
    }

    protected Cell header(Cell cell, SearchResultPresentation presentation, String localizationKey) {
        Locale locale = presentation.getLocale();
        if (locale == null) {
            locale = DEFAULT_LOCALE;
        }
        String value = this.messageService.getMessage(localizationKey, locale);
        OphCellStyles ophCellStyles = new OphCellStyles(cell.getSheet().getWorkbook());
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setBold(true);
        ophCellStyles.visit(cellStyle -> cellStyle.setFont(font));
        if (value != null) {
            cell.setCellValue(value);
        }
        ophCellStyles.apply(cell);
        return cell;
    }

    protected Cell cell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        return cell;
    }
}
