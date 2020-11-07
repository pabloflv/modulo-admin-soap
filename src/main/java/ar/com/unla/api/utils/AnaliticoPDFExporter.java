package ar.com.unla.api.utils;

import ar.com.unla.api.models.database.Usuario;
import ar.com.unla.api.models.database.UsuarioExamenFinal;
import ar.com.unla.api.models.database.UsuarioMateria;
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
public class AnaliticoPDFExporter {


    private List<UsuarioMateria> materias;

    private List<UsuarioExamenFinal> finales;

    private Usuario usuario;


    public void export(HttpServletResponse response) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        writeTableTitle(document, "Analítico UNLa");

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{18f, 12f, 14f, 14f, 14f, 12f, 14f});

        String usuario =
                this.usuario.getApellido() + " " + this.usuario.getNombre() + " (DNI: "
                        + this.usuario
                        .getDni() + ")";

        writeTableTitle(document, usuario);
        writeTableHeader(tabla);
        writeTableData(tabla, materias, finales);
        document.add(tabla);
        document.close();
    }

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

        cell.setPhrase(new Phrase("Materia", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nota TP", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nota Parcial", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Promedio Materia", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Condición", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nota final", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Promedio General", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, List<UsuarioMateria> materias,
            List<UsuarioExamenFinal> finales) {

        String condicion;
        float promedioMateria = 0;

        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Paragraph.ALIGN_CENTER);

        if ((finales != null && !finales.isEmpty()) && (materias != null)) {

            for (UsuarioExamenFinal usuarioExamenFinal : Collections.unmodifiableList(finales)) {
                boolean libre = false;
                boolean materiaEcontrada = false;

                if (usuarioExamenFinal.getCalificacion() >= 4) {
                    long idMateria =
                            usuarioExamenFinal.getExamenFinal().getMateria().getId();
                    String nombreMateria =
                            usuarioExamenFinal.getExamenFinal().getMateria().getNombre();

                    cell.setPhrase(new Phrase(String.valueOf(nombreMateria)));
                    table.addCell(cell);
                    if (!materias.isEmpty()) {
                        for (UsuarioMateria usuarioMateria : Collections
                                .unmodifiableList(materias)) {
                            if (usuarioMateria.getMateria().getId().equals(idMateria)) {
                                materiaEcontrada = true;
                                promedioMateria =
                                        (usuarioMateria.getCalificacionTps() + usuarioMateria
                                                .getCalificacionExamen()) / 2;

                                if (promedioMateria >= 4) {
                                    cell.setPhrase(
                                            new Phrase(String.valueOf(
                                                    usuarioMateria.getCalificacionTps())));
                                    table.addCell(cell);

                                    cell.setPhrase(
                                            new Phrase(String.valueOf(
                                                    usuarioMateria.getCalificacionExamen())));
                                    table.addCell(cell);

                                    cell.setPhrase(new Phrase(String.valueOf(promedioMateria)));
                                    table.addCell(cell);
                                    libre = false;
                                    break;
                                } else {
                                    setFreeSubject(table, cell);
                                    libre = true;
                                    break;
                                }
                            }
                        }
                        if (!materiaEcontrada) {
                            setFreeSubject(table, cell);
                            libre = true;
                        }

                    } else {
                        setFreeSubject(table, cell);
                        libre = true;
                    }

                    float promediofinal;

                    if (libre) {
                        condicion = "Libre";
                        promediofinal = usuarioExamenFinal.getCalificacion();
                    } else {
                        condicion = "Regular";
                        promediofinal =
                                (promedioMateria + usuarioExamenFinal.getCalificacion()) / 2;
                    }

                    cell.setPhrase(new Phrase(condicion));
                    table.addCell(cell);

                    cell.setPhrase(
                            new Phrase(String.valueOf(usuarioExamenFinal.getCalificacion())));
                    table.addCell(cell);

                    cell.setPhrase(new Phrase(String.valueOf(promediofinal)));
                    table.addCell(cell);
                }
            }
        }
    }

    private void setFreeSubject(PdfPTable table, PdfPCell cell) {
        cell.setPhrase(new Phrase("-"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("-"));
        table.addCell(cell);

        cell.setPhrase(new Phrase("-"));
        table.addCell(cell);
    }
}
