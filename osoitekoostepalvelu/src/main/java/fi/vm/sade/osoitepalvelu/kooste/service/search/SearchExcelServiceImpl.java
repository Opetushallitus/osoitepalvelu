package fi.vm.sade.osoitepalvelu.kooste.service.search;

import com.google.common.base.Optional;
import fi.vm.sade.javautils.poi.OphCellStyles;
import fi.vm.sade.osoitepalvelu.kooste.service.AbstractService;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultOsoiteDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultRowDto;
import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsPresentationDto;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static fi.vm.sade.osoitepalvelu.kooste.common.util.StringHelper.join;


@Service
public class SearchExcelServiceImpl extends AbstractService implements SearchExcelService {


    @Autowired
    private MessageService messageService;

    @Override
    public void produceExcel(HSSFWorkbook workbook, SearchResultsPresentationDto searchResults) {
        HSSFSheet sheet = workbook.createSheet();

        int rowNum = 0;
        int maxColumn = produceHeader(sheet, rowNum, 0, searchResults.getPresentation());
        OphCellStyles.OphHssfCellStyles ophHssfCellStyles = new OphCellStyles.OphHssfCellStyles(workbook);
        for (SearchResultRowDto row : searchResults.getRows()) {
            produceRow(searchResults.getPresentation(), sheet, ++rowNum, row, ophHssfCellStyles);
        }
        for (int i = 0; i < maxColumn; ++i) {
            sheet.autoSizeColumn(i);
        }
    }

    protected int produceHeader(HSSFSheet sheet, int rowNum, int cellNum, SearchResultPresentation presentation) {
        if (presentation.isOrganisaationNimiIncluded()) {
            header(cell(sheet, rowNum, cellNum++), presentation, "result_excel_organisaatio_nimi");
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

    private void produceRow(SearchResultPresentation presentation, HSSFSheet sheet, int rowNum, SearchResultRowDto row, OphCellStyles.OphHssfCellStyles ophHssfCellStyles) {
        int cellNum = 0;
        if (presentation.isOrganisaationNimiIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getNimi(), ophHssfCellStyles);
        }
        if (presentation.isOrganisaatiotunnisteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getOppilaitosKoodi(), ophHssfCellStyles);
        }
        if (presentation.isYtunnusIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getYtunnus(), ophHssfCellStyles);
        }
        if (presentation.isOpetuskieliIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getOpetuskieli(), ophHssfCellStyles);
        }
        if (presentation.isYritysmuotoIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getYritysmuoto(), ophHssfCellStyles);
        }
        if (presentation.isYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), join(" ", row.getYhteystietoNimi()), ophHssfCellStyles);
        }
        if (presentation.isYhteyshenkiloEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getHenkiloEmail(), ophHssfCellStyles);
        }
        if (presentation.isOrganisaatioEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getEmailOsoite(), ophHssfCellStyles);
        }
        if (presentation.isPositosoiteIncluded()) {
            SearchResultOsoiteDto osoite = row.getPostiosoite();
            value(cell(sheet, rowNum, cellNum++), osoite(osoite), ophHssfCellStyles);
            String postinumero = null,
                    postitoimipaikka = null;
            if (osoite != null) {
                postinumero = osoite.getPostinumero();
                postitoimipaikka = osoite.getPostitoimipaikka();
            }
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postinumero).or(""), ophHssfCellStyles);
            value(cell(sheet, rowNum, cellNum++), Optional.fromNullable(postitoimipaikka).or(""), ophHssfCellStyles);
        }
        if (presentation.isPuhelinnumeroIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getPuhelinnumero(), ophHssfCellStyles);
        }
        if (presentation.isWwwOsoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getWwwOsoite(), ophHssfCellStyles);
        }
        if (presentation.isViranomaistiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getViranomaistiedotuksenEmail(), ophHssfCellStyles);
        }
        if (presentation.isKoulutusneuvonnanSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKoulutusneuvonnanEmail(), ophHssfCellStyles);
        }
        if (presentation.isKriisitiedotuksenSahkopostiosoiteIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKriisitiedotuksenEmail(), ophHssfCellStyles);
        }
        if(presentation.isVarhaiskasvatuksenYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getVarhaiskasvatuksenYhteyshenkilo(), ophHssfCellStyles);
        }
        if(presentation.isVarhaiskasvatuksenEmailIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getVarhaiskasvatuksenEmail(), ophHssfCellStyles);
        }
        if(presentation.isKoskiYhdyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKoskiYhdyshenkilo(), ophHssfCellStyles);
        }
        if (presentation.isMoveYhteyshenkiloIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getMoveYhteyshenkilo(), ophHssfCellStyles);
        }
        if (presentation.isOrganisaationSijaintikuntaIncluded()) {
            value(cell(sheet, rowNum, cellNum++), row.getKotikunta(), ophHssfCellStyles);
        }

    }

    protected HSSFCell value(HSSFCell cell, String value, OphCellStyles.OphHssfCellStyles ophHssfCellStyles) {
        if (value != null) {
            cell.setCellValue(value);
        }
        ophHssfCellStyles.apply(cell);
        return cell;
    }

    protected Cell header(HSSFCell cell, SearchResultPresentation presentation, String localizationKey) {
        Locale locale = presentation.getLocale();
        if (locale == null) {
            locale = DEFAULT_LOCALE;
        }
        String value = this.messageService.getMessage(localizationKey, locale);
        OphCellStyles.OphHssfCellStyles ophCellStyles = new OphCellStyles.OphHssfCellStyles(cell.getSheet().getWorkbook());
        Font font = cell.getSheet().getWorkbook().createFont();
        font.setBold(true);
        ophCellStyles.visit(hssfCellStyle -> hssfCellStyle.setFont(font));
        if (value != null) {
            cell.setCellValue(value);
        }
        ophCellStyles.apply(cell);
        return cell;
    }

    protected HSSFCell cell(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        HSSFCell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        return cell;
    }
}
