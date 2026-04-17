package ateneu.sgcti.gchamados.relatorios.service;

import ateneu.sgcti.gchamados.entity.ChamadoEntity;
import ateneu.sgcti.gchamados.relatorios.dto.RelatorioPdfFiltro;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ChamadoPdfGenerator {

    private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] gerarRelatorioChamadosPdf(List<ChamadoEntity> chamados, RelatorioPdfFiltro filtro) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, output);
            document.open();

            Font titulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font subtitulo = new Font(Font.HELVETICA, 11, Font.NORMAL);
            Font cabecalhoTabela = new Font(Font.HELVETICA, 10, Font.BOLD);

            document.add(new Paragraph("Relatorio de Chamados", titulo));
            document.add(new Paragraph("Gerado em: " + formatarData(LocalDateTime.now()), subtitulo));
            document.add(new Paragraph("Filtros: " + formatarFiltros(filtro), subtitulo));
            document.add(new Paragraph("Total de chamados: " + chamados.size(), subtitulo));
            document.add(new Paragraph(" "));

            if (chamados.isEmpty()) {
                document.add(new Paragraph("Nenhum chamado encontrado para os filtros informados."));
                document.close();
                return output.toByteArray();
            }

            PdfPTable table = new PdfPTable(new float[]{0.7f, 1.4f, 1.0f, 1.0f, 1.6f, 1.6f, 1.2f, 1.2f});
            table.setWidthPercentage(100);

            adicionarCabecalho(table, "ID", cabecalhoTabela);
            adicionarCabecalho(table, "Titulo", cabecalhoTabela);
            adicionarCabecalho(table, "Status", cabecalhoTabela);
            adicionarCabecalho(table, "Prioridade", cabecalhoTabela);
            adicionarCabecalho(table, "Solicitante", cabecalhoTabela);
            adicionarCabecalho(table, "Tecnico", cabecalhoTabela);
            adicionarCabecalho(table, "Abertura", cabecalhoTabela);
            adicionarCabecalho(table, "Fechamento", cabecalhoTabela);

            for (ChamadoEntity chamado : chamados) {
                table.addCell(valor(chamado.getId()));
                table.addCell(valor(chamado.getTitulo()));
                table.addCell(valor(chamado.getStatus()));
                table.addCell(valor(chamado.getPrioridade()));
                table.addCell(valor(chamado.getSolicitanteEntity().getUsuarioEntity().getNome()));
                table.addCell(chamado.getTecnicoEntity() == null
                        ? "Nao atribuido"
                        : valor(chamado.getTecnicoEntity().getUsuarioEntity().getNome()));
                table.addCell(formatarData(chamado.getDataAbertura()));
                table.addCell(formatarData(chamado.getDataFechamento()));
            }

            document.add(table);
            document.close();
            return output.toByteArray();
        } catch (DocumentException ex) {
            throw new IllegalStateException("Falha ao gerar PDF de chamados.", ex);
        }
    }

    private void adicionarCabecalho(PdfPTable table, String titulo, Font fonte) {
        PdfPCell cell = new PdfPCell(new Phrase(titulo, fonte));
        table.addCell(cell);
    }

    private String formatarData(LocalDateTime data) {
        return data == null ? "-" : DATA_HORA_FORMATTER.format(data);
    }

    private String formatarFiltros(RelatorioPdfFiltro filtro) {
        return "status=" + valor(filtro.status())
                + ", prioridade=" + valor(filtro.prioridade())
                + ", tecnicoId=" + valor(filtro.tecnicoId())
                + ", solicitanteId=" + valor(filtro.solicitanteId())
                + ", inicio=" + valor(filtro.inicio())
                + ", fim=" + valor(filtro.fim());
    }

    private String valor(Object valor) {
        return valor == null ? "todos" : valor.toString();
    }
}


