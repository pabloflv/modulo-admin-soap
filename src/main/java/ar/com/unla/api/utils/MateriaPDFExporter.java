package ar.com.unla.api.utils;

import ar.com.unla.api.models.database.HorarioMateria;
import ar.com.unla.api.models.database.Materia;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MateriaPDFExporter {

    private List<Materia> materiasMañana;

    private List<Materia> materiasTarde;

    private List<Materia> materiasNoche;

    private void writeTableTitle(Document document, String titulo) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(new Color(138, 45, 45));
        font.setSize(18);

        Paragraph title = new Paragraph(titulo, font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(15);
        document.add(title);
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(138, 45, 45));
        cell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Año", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Cuatrimestre", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Materia", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Turno", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Horario", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<Materia> materias) {

        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);

        for (Materia materia : Collections.unmodifiableList(materias)) {
            cell.setPhrase(new Phrase(String.valueOf(materia.getAnioCarrera())));
            table.addCell(cell);

            cell.setPhrase(new Phrase(materia.getCuatrimestre() == 1 ? "Primer Cuatrimestre"
                    : "Segundo Cuatrimestre"));
            table.addCell(cell);

            cell.setPhrase(new Phrase(materia.getNombre()));
            table.addCell(cell);

            cell.setPhrase(new Phrase(materia.getTurno().getDescripcion()));
            table.addCell(cell);

            StringBuilder dias = new StringBuilder();

            if (materia.getHorarios() != null && !materia.getHorarios().isEmpty()) {
                for (HorarioMateria hm : materia.getHorarios()) {
                    String horarios =
                            hm.getDia() + " - " + hm.getHoraDesde() + " " + hm.getHoraHasta()
                                    + "\n";
                    dias.append(horarios);
                }
            }

            cell.setPhrase(new Phrase(String.valueOf(dias)));
            table.addCell(cell);
        }

    }

    public void export(HttpServletResponse response) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        writeTableTitle(document, "UNLa");

        PdfPTable tablaManiana = new PdfPTable(5);
        tablaManiana.setWidthPercentage(100);

        writeTableTitle(document, "Turno mañana");
        writeTableHeader(tablaManiana);
        writeTableData(tablaManiana, materiasMañana);
        document.add(tablaManiana);

        PdfPTable tablaTarde = new PdfPTable(5);
        tablaTarde.setWidthPercentage(100);

        writeTableTitle(document, "Turno tarde");
        writeTableHeader(tablaTarde);
        writeTableData(tablaTarde, materiasTarde);
        document.add(tablaTarde);

        PdfPTable tablaNoche = new PdfPTable(5);
        tablaNoche.setWidthPercentage(100);

        writeTableTitle(document, "Turno noche");
        writeTableHeader(tablaNoche);
        writeTableData(tablaNoche, materiasNoche);
        document.add(tablaNoche);

        document.close();
    }

}
