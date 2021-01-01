package com.backbase.oss.boat.bay.service.export;

import com.backbase.oss.boat.ExportException;
import com.backbase.oss.boat.bay.domain.Portal;
import java.io.OutputStream;

public interface Exporter {

    ExportInfo export(ExportOptions exportOptions) throws ExportException;

    ExportType getExportType();

}
