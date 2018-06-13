package fi.vm.sade.osoitepalvelu.kooste.service.search;

import fi.vm.sade.osoitepalvelu.kooste.service.search.dto.SearchResultsPresentationDto;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface SearchExcelService {
    /**
     * @param workbook the excel workbook to produce the rows to
     * @param searchResults to produce to the workbook
     */
    void produceExcel(HSSFWorkbook workbook, SearchResultsPresentationDto searchResults);
}
