package com.backbase.oss.boat.bay.service.export;

import com.backbase.oss.boat.bay.domain.Portal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportOptions {

    private Portal portal;
    private ExportType exportType;
    private String location;

}
