package com.ca.cafe_uni.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReporteExcelService {

    private final ReporteService reporteService;

    public byte[] generarReporteCompleto() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {

            CellStyle estiloTitulo = crearEstiloTitulo(workbook);
            CellStyle estiloHeader = crearEstiloHeader(workbook);

            // ── Hoja 1: Mejores calificados de hoy ──
            Sheet hoja1 = workbook.createSheet("Calificaciones Hoy");
            llenarHojaCalificaciones(hoja1, reporteService.mejoresCalificadosHoy(),
                    estiloTitulo, estiloHeader);

            // ── Hoja 2: Mejores calificados de la semana ──
            Sheet hoja2 = workbook.createSheet("Calificaciones Semana");
            llenarHojaTexto(hoja2, reporteService.mejoresCalificadosSemana(),
                    "Mejor plato calificado por día", "Día", "Plato",
                    estiloTitulo, estiloHeader);

            // ── Hoja 3: Visitas por día ──
            Sheet hoja3 = workbook.createSheet("Visitas");
            llenarHojaLong(hoja3, reporteService.visitasUltimos7Dias(),
                    "Visitas de los últimos 7 días", "Fecha", "Cantidad",
                    estiloTitulo, estiloHeader);

            // ── Hoja 4: Visitas por dispositivo ──
            Sheet hoja4 = workbook.createSheet("Dispositivos");
            llenarHojaLong(hoja4, reporteService.visitasPorDispositivo(),
                    "Visitas por tipo de dispositivo", "Dispositivo", "Cantidad",
                    estiloTitulo, estiloHeader);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void llenarHojaCalificaciones(Sheet hoja, Map<String, Double> datos,
                                          CellStyle estiloTitulo, CellStyle estiloHeader) {
        int filaIdx = 0;

        Row titulo = hoja.createRow(filaIdx++);
        Cell tituloCell = titulo.createCell(0);
        tituloCell.setCellValue("Mejores platos calificados hoy");
        tituloCell.setCellStyle(estiloTitulo);
        filaIdx++;

        Row header = hoja.createRow(filaIdx++);
        crearCelda(header, 0, "Plato", estiloHeader);
        crearCelda(header, 1, "Calificación promedio", estiloHeader);

        for (Map.Entry<String, Double> entry : datos.entrySet()) {
            Row fila = hoja.createRow(filaIdx++);
            fila.createCell(0).setCellValue(entry.getKey());
            fila.createCell(1).setCellValue(entry.getValue());
        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
    }

    private void llenarHojaTexto(Sheet hoja, Map<String, String> datos, String titulo,
                                 String col1, String col2,
                                 CellStyle estiloTitulo, CellStyle estiloHeader) {
        int filaIdx = 0;

        Row filaTitulo = hoja.createRow(filaIdx++);
        Cell tituloCell = filaTitulo.createCell(0);
        tituloCell.setCellValue(titulo);
        tituloCell.setCellStyle(estiloTitulo);
        filaIdx++;

        Row header = hoja.createRow(filaIdx++);
        crearCelda(header, 0, col1, estiloHeader);
        crearCelda(header, 1, col2, estiloHeader);

        for (Map.Entry<String, String> entry : datos.entrySet()) {
            Row fila = hoja.createRow(filaIdx++);
            fila.createCell(0).setCellValue(entry.getKey());
            fila.createCell(1).setCellValue(entry.getValue());
        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
    }

    private void llenarHojaLong(Sheet hoja, Map<String, Long> datos, String titulo,
                                String col1, String col2,
                                CellStyle estiloTitulo, CellStyle estiloHeader) {
        int filaIdx = 0;

        Row filaTitulo = hoja.createRow(filaIdx++);
        Cell tituloCell = filaTitulo.createCell(0);
        tituloCell.setCellValue(titulo);
        tituloCell.setCellStyle(estiloTitulo);
        filaIdx++;

        Row header = hoja.createRow(filaIdx++);
        crearCelda(header, 0, col1, estiloHeader);
        crearCelda(header, 1, col2, estiloHeader);

        for (Map.Entry<String, Long> entry : datos.entrySet()) {
            Row fila = hoja.createRow(filaIdx++);
            fila.createCell(0).setCellValue(entry.getKey());
            fila.createCell(1).setCellValue(entry.getValue());
        }

        hoja.autoSizeColumn(0);
        hoja.autoSizeColumn(1);
    }

    private void crearCelda(Row fila, int col, String valor, CellStyle estilo) {
        Cell celda = fila.createCell(col);
        celda.setCellValue(valor);
        celda.setCellStyle(estilo);
    }

    private CellStyle crearEstiloTitulo(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();
        Font fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setFontHeightInPoints((short) 14);
        estilo.setFont(fuente);
        return estilo;
    }

    private CellStyle crearEstiloHeader(Workbook workbook) {
        CellStyle estilo = workbook.createCellStyle();
        Font fuente = workbook.createFont();
        fuente.setBold(true);
        fuente.setColor(IndexedColors.WHITE.getIndex());
        estilo.setFont(fuente);
        estilo.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return estilo;
    }
}