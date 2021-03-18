package com.backbase.oss.boat.bay.service.export;

import com.backbase.oss.boat.ExportException;

public interface Exporter {

    ExportInfo export(ExportOptions exportOptions) throws ExportException;

    ExportType getExportType();

}
