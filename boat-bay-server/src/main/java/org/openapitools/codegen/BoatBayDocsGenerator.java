package org.openapitools.codegen;

import com.backbase.oss.codegen.doc.BoatDocsGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.utils.ModelUtils;

public class BoatBayDocsGenerator extends DefaultGenerator {


    public Map<String, Object> processModel() {



        List<File> files = new ArrayList<>();
        // models
        List<String> filteredSchemas = ModelUtils.getSchemasUsedOnlyInFormParam(openAPI);
        List<Object> allModels = new ArrayList<>();


        super.generateModels(files, allModels, filteredSchemas);

        return new HashMap<>();

    }
}
