package ar.com.unla.api.utils;

import ar.com.unla.api.dtos.response.AlumnoFinalDTO;
import ar.com.unla.api.dtos.response.AlumnosFinalDTO;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.DecoderException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AlumnosFinalesExcelExporter {

    private AlumnosFinalDTO alumnosFinal;

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    public AlumnosFinalesExcelExporter(AlumnosFinalDTO alumnosFinal) {
        this.alumnosFinal = alumnosFinal;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(
                "Final " + alumnosFinal.getExamenFinal().getMateria().getNombre());
    }

    public void export(HttpServletResponse response) throws IOException, DecoderException {
        writeHeaderRow();
        writeDataRows();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private void writeHeaderRow() throws DecoderException {

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeight(22);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row rowTitle = sheet.createRow(0);
        Cell cell = rowTitle.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.getSheet().addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        int numMerged = cell.getSheet().getNumMergedRegions();
        for (int i = 0; i < numMerged; i++) {
            CellRangeAddress mergedRegions = cell.getSheet().getMergedRegion(i);
            RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegions, cell.getSheet());
            RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegions, cell.getSheet());
            RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegions, cell.getSheet());
            RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegions, cell.getSheet());
            RegionUtil.setBottomBorderColor(IndexedColors.BLACK.getIndex(), mergedRegions,
                    cell.getSheet());
            RegionUtil.setTopBorderColor(IndexedColors.BLACK.getIndex(), mergedRegions,
                    cell.getSheet());
            RegionUtil.setRightBorderColor(IndexedColors.BLACK.getIndex(), mergedRegions,
                    cell.getSheet());
            RegionUtil.setLeftBorderColor(IndexedColors.BLACK.getIndex(), mergedRegions,
                    cell.getSheet());
        }

        cell.setCellValue(
                "Final " + alumnosFinal.getExamenFinal().getMateria().getNombre() + " - "
                        + alumnosFinal.getExamenFinal().getMateria().getId());

        Row rowData = sheet.createRow(1);
        String[] columns =
                {"Codigo usuario", "Nombre y apellido", "DNI", "Nota final"};

        for (int i = 0; i < columns.length; i++) {
            cell = rowData.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }

    }

    private void writeDataRows() {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setFontHeight(20);
        cellStyle.setFont(font);

        int numRow = 2;
        for (AlumnoFinalDTO alumno : alumnosFinal.getAlumnos()) {
            Row rowDataValue = sheet.createRow(numRow);
            Cell idCell = rowDataValue.createCell(0);
            idCell.setCellStyle(cellStyle);
            idCell.setCellValue(alumno.getUsuario().getId());
            sheet.autoSizeColumn(0);

            Cell nameCell = rowDataValue.createCell(1);
            nameCell.setCellValue(
                    alumno.getUsuario().getNombre() + " " + alumno.getUsuario().getApellido());
            nameCell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(1);

            Cell dniCell = rowDataValue.createCell(2);
            dniCell.setCellValue(alumno.getUsuario().getDni());
            dniCell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(2);

            Cell examCell = rowDataValue.createCell(3);
            examCell.setCellStyle(cellStyle);

            numRow++;
        }
    }
}
