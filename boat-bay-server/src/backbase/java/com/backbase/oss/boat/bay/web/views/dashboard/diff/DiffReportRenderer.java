package com.backbase.oss.boat.bay.web.views.dashboard.diff;

import static j2html.TagCreator.*;
import static org.openapitools.openapidiff.core.model.Changed.result;

import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import j2html.tags.ContainerTag;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.openapitools.openapidiff.core.model.*;
import org.openapitools.openapidiff.core.output.Render;
import org.openapitools.openapidiff.core.utils.RefPointer;
import org.openapitools.openapidiff.core.utils.RefType;

public class DiffReportRenderer implements Render {

    private static final RefPointer<Schema> refPointer = new RefPointer<>(RefType.SCHEMAS);

    protected ChangedOpenApi diff;

    public DiffReportRenderer() {}

    public String render(ChangedOpenApi diff) {
        this.diff = diff;

        List<Endpoint> newEndpoints = diff.getNewEndpoints();
        ContainerTag ol_newEndpoint = ol_newEndpoint(newEndpoints);

        List<Endpoint> missingEndpoints = diff.getMissingEndpoints();
        ContainerTag ol_missingEndpoint = ol_missingEndpoint(missingEndpoints);

        List<Endpoint> deprecatedEndpoints = diff.getDeprecatedEndpoints();
        ContainerTag ol_deprecatedEndpoint = ol_deprecatedEndpoint(deprecatedEndpoints);

        List<ChangedOperation> changedOperations = diff.getChangedOperations();
        ContainerTag ol_changed = ol_changed(changedOperations);

        return renderHtml(ol_newEndpoint, ol_missingEndpoint, ol_deprecatedEndpoint, ol_changed, null);
    }

    public String render(ChangedOpenApi diff, Map<String, Map<PathItem.HttpMethod, List<String>>> changedTags) {
        this.diff = diff;

        List<Endpoint> newEndpoints = diff.getNewEndpoints();
        ContainerTag olNewEndpoint = ol_newEndpoint(newEndpoints);

        List<Endpoint> missingEndpoints = diff.getMissingEndpoints();
        ContainerTag olMissingEndpoint = ol_missingEndpoint(missingEndpoints);

        List<Endpoint> deprecatedEndpoints = diff.getDeprecatedEndpoints();
        ContainerTag olDeprecatedEndpoint = ol_deprecatedEndpoint(deprecatedEndpoints);

        List<ChangedOperation> changedOperations = diff.getChangedOperations();
        ContainerTag olChanged = ol_changed(changedOperations);

        ContainerTag olTags = olTags(changedTags);

        return renderHtml(olNewEndpoint, olMissingEndpoint, olDeprecatedEndpoint, olChanged, olTags);
    }

    public String renderHtml(ContainerTag olNew, ContainerTag olMiss, ContainerTag olDeprec, ContainerTag olChanged, ContainerTag olTags) {
        ContainerTag html = div()
            .withClass("article")
            .with(
                div().with(h5("What's New"), hr(), olNew),
                div().with(h5("What's Deleted"), hr(), olMiss),
                div().with(h5("What's Deprecated"), hr(), olDeprec),
                div().with(h5("What's Changed"), hr(), olChanged)
            );
        if (olTags != null) {
            html.with(div().with(h5("Missing Tags"), hr(), olTags));
        }

        return html.render();
    }

    private ContainerTag ol_newEndpoint(List<Endpoint> endpoints) {
        if (null == endpoints) return ol();
        ContainerTag ol = ol();
        for (Endpoint endpoint : endpoints) {
            ol.with(li_newEndpoint(endpoint.getMethod().toString(), endpoint.getPathUrl(), endpoint.getSummary()));
        }
        return ol;
    }

    private ContainerTag li_newEndpoint(String method, String path, String desc) {
        return li().with(span(method).withClass(method)).withText(path + " ").with(span(desc));
    }

    private ContainerTag ol_missingEndpoint(List<Endpoint> endpoints) {
        if (null == endpoints) return ol();
        ContainerTag ol = ol();
        for (Endpoint endpoint : endpoints) {
            ol.with(li_missingEndpoint(endpoint.getMethod().toString(), endpoint.getPathUrl(), endpoint.getSummary()));
        }
        return ol;
    }

    private ContainerTag li_missingEndpoint(String method, String path, String desc) {
        return li().with(span(method).withClass(method), del().withText(path)).with(span(" " + desc));
    }

    private ContainerTag ol_deprecatedEndpoint(List<Endpoint> endpoints) {
        if (null == endpoints) return ol();
        ContainerTag ol = ol();
        for (Endpoint endpoint : endpoints) {
            ol.with(li_deprecatedEndpoint(endpoint.getMethod().toString(), endpoint.getPathUrl(), endpoint.getSummary()));
        }
        return ol;
    }

    private ContainerTag li_deprecatedEndpoint(String method, String path, String desc) {
        return li().with(span(method).withClass(method), del().withText(path)).with(span(" " + desc));
    }

    private ContainerTag ol_changed(List<ChangedOperation> changedOperations) {
        if (null == changedOperations) return ol();
        ContainerTag ol = ol();
        for (ChangedOperation changedOperation : changedOperations) {
            String pathUrl = changedOperation.getPathUrl();
            String method = changedOperation.getHttpMethod().toString();
            String desc = Optional.ofNullable(changedOperation.getSummary()).map(ChangedMetadata::getRight).orElse("");

            ContainerTag ul_detail = ul().withClass("detail");
            if (result(changedOperation.getParameters()).isDifferent()) {
                ul_detail.with(li().with(h6("Parameters")).with(ul_param(changedOperation.getParameters())));
            }
            if (changedOperation.resultRequestBody().isDifferent()) {
                ul_detail.with(li().with(h6("Request")).with(ul_request(changedOperation.getRequestBody().getContent())));
            }
            if (changedOperation.resultApiResponses().isDifferent()) {
                ul_detail.with(li().with(h6("Response")).with(ul_response(changedOperation.getApiResponses())));
            }
            ol.with(li().with(span(method).withClass(method)).withText(pathUrl + " ").with(span(desc)).with(ul_detail));
        }
        return ol;
    }

    private ContainerTag olTags(Map<String, Map<PathItem.HttpMethod, List<String>>> changedTags) {
        ContainerTag ol = ol();
        for (var eachPath : changedTags.entrySet()) {
            String pathUrl = eachPath.getKey();
            for (var eachHttpMethod : eachPath.getValue().entrySet()) {
                PathItem.HttpMethod httpMethod = eachHttpMethod.getKey();
                ol.with(liMissingTags(pathUrl, httpMethod.toString(), changedTags.get(pathUrl).get(httpMethod)));
            }
        }
        return ol;
    }

    private ContainerTag liMissingTags(String pathUrl, String methodName, List<String> missingTags) {
        return li()
            .with(span(methodName).withClass(methodName))
            .withText(pathUrl)
            .with(ul().withClass("detail").with(li().with(span(missingTags.toString()))));
    }

    private ContainerTag ul_response(ChangedApiResponse changedApiResponse) {
        Map<String, ApiResponse> addResponses = changedApiResponse.getIncreased();
        Map<String, ApiResponse> delResponses = changedApiResponse.getMissing();
        Map<String, ChangedResponse> changedResponses = changedApiResponse.getChanged();
        ContainerTag ul = ul().withClass("change response");
        for (String propName : addResponses.keySet()) {
            ul.with(li_addResponse(propName, addResponses.get(propName)));
        }
        for (String propName : delResponses.keySet()) {
            ul.with(li_missingResponse(propName, delResponses.get(propName)));
        }
        for (String propName : changedResponses.keySet()) {
            ul.with(li_changedResponse(propName, changedResponses.get(propName)));
        }
        return ul;
    }

    private ContainerTag li_addResponse(String name, ApiResponse response) {
        return li()
            .withText(String.format("New response : [%s]", name))
            .with(span(null == response.getDescription() ? "" : ("//" + response.getDescription())).withClass("comment"));
    }

    private ContainerTag li_missingResponse(String name, ApiResponse response) {
        return li()
            .withText(String.format("Deleted response : [%s]", name))
            .with(span(null == response.getDescription() ? "" : ("//" + response.getDescription())).withClass("comment"));
    }

    private ContainerTag li_changedResponse(String name, ChangedResponse response) {
        return li()
            .withText(String.format("Changed response : [%s]", name))
            .with(
                span(
                    (null == response.getNewApiResponse() || null == response.getNewApiResponse().getDescription())
                        ? ""
                        : ("//" + response.getNewApiResponse().getDescription())
                )
                    .withClass("comment")
            )
            .with(ul_request(response.getContent()));
    }

    private ContainerTag ul_request(ChangedContent changedContent) {
        ContainerTag ul = ul().withClass("change request-body");
        if (changedContent != null) {
            for (String propName : changedContent.getIncreased().keySet()) {
                ul.with(li_addRequest(propName, changedContent.getIncreased().get(propName)));
            }
            for (String propName : changedContent.getMissing().keySet()) {
                ul.with(li_missingRequest(propName, changedContent.getMissing().get(propName)));
            }
            for (String propName : changedContent.getChanged().keySet()) {
                ul.with(li_changedRequest(propName, changedContent.getChanged().get(propName)));
            }
        }
        return ul;
    }

    private ContainerTag li_addRequest(String name, MediaType request) {
        return li().withText(String.format("New body: '%s'", name));
    }

    private ContainerTag li_missingRequest(String name, MediaType request) {
        return li().withText(String.format("Deleted body: '%s'", name));
    }

    private ContainerTag li_changedRequest(String name, ChangedMediaType request) {
        ContainerTag li = li().with(div_changedSchema(request.getSchema())).withText(String.format("Changed body: '%s'", name));
        if (request.isIncompatible()) {
            incompatibilities(li, request.getSchema());
        }
        return li;
    }

    private ContainerTag div_changedSchema(ChangedSchema schema) {
        ContainerTag div = div();
        div.with(h6("Schema" + (schema.isIncompatible() ? " incompatible" : "")));
        return div;
    }

    private void incompatibilities(final ContainerTag output, final ChangedSchema schema) {
        incompatibilities(output, "", schema);
    }

    private void incompatibilities(final ContainerTag output, String propName, final ChangedSchema schema) {
        if (schema.getItems() != null) {
            items(output, propName, schema.getItems());
        }
        if (schema.isCoreChanged() == DiffResult.INCOMPATIBLE && schema.isChangedType()) {
            String type = type(schema.getOldSchema()) + " -> " + type(schema.getNewSchema());
            property(output, propName, "Changed property type", type);
        }
        String prefix = propName.isEmpty() ? "" : propName + ".";
        properties(output, prefix, "Missing property", schema.getMissingProperties(), schema.getContext());
        schema.getChangedProperties().forEach((name, property) -> incompatibilities(output, prefix + name, property));
    }

    private void items(ContainerTag output, String propName, ChangedSchema schema) {
        incompatibilities(output, propName + "[n]", schema);
    }

    private void properties(ContainerTag output, String propPrefix, String title, Map<String, Schema> properties, DiffContext context) {
        if (properties != null) {
            properties.forEach((key, value) -> resolveProperty(output, propPrefix, key, value, title));
        }
    }

    private void resolveProperty(ContainerTag output, String propPrefix, String key, Schema value, String title) {
        try {
            property(output, propPrefix + key, title, resolve(value));
        } catch (Exception e) {
            property(output, propPrefix + key, title, type(value));
        }
    }

    protected void property(ContainerTag output, String name, String title, Schema schema) {
        property(output, name, title, type(schema));
    }

    protected void property(ContainerTag output, String name, String title, String type) {
        output.with(p(String.format("%s: %s (%s)", title, name, type)).withClass("missing"));
    }

    protected Schema resolve(Schema schema) {
        return refPointer.resolveRef(diff.getNewSpecOpenApi().getComponents(), schema, schema.get$ref());
    }

    protected String type(Schema schema) {
        String result = "object";
        if (schema == null) {
            result = "no schema";
        } else if (schema instanceof ArraySchema) {
            result = "array";
        } else if (schema.getType() != null) {
            result = schema.getType();
        }
        return result;
    }

    private ContainerTag ul_param(ChangedParameters changedParameters) {
        List<Parameter> addParameters = changedParameters.getIncreased();
        List<Parameter> delParameters = changedParameters.getMissing();
        List<ChangedParameter> changed = changedParameters.getChanged();
        ContainerTag ul = ul().withClass("change param");
        for (Parameter param : addParameters) {
            ul.with(li_addParam(param));
        }
        for (ChangedParameter param : changed) {
            ul.with(li_changedParam(param));
        }
        for (Parameter param : delParameters) {
            ul.with(li_missingParam(param));
        }
        return ul;
    }

    private ContainerTag li_addParam(Parameter param) {
        return li()
            .withText("Add " + param.getName() + " in " + param.getIn())
            .with(span(null == param.getDescription() ? "" : ("//" + param.getDescription())).withClass("comment"));
    }

    private ContainerTag li_missingParam(Parameter param) {
        return li()
            .withClass("missing")
            .with(span("Delete"))
            .with(del(param.getName()))
            .with(span("in ").withText(param.getIn()))
            .with(span(null == param.getDescription() ? "" : ("//" + param.getDescription())).withClass("comment"));
    }

    private ContainerTag li_deprecatedParam(ChangedParameter param) {
        return li()
            .withClass("missing")
            .with(span("Deprecated"))
            .with(del(param.getName()))
            .with(span("in ").withText(param.getIn()))
            .with(
                span(null == param.getNewParameter().getDescription() ? "" : ("//" + param.getNewParameter().getDescription()))
                    .withClass("comment")
            );
    }

    private ContainerTag li_changedParam(ChangedParameter changeParam) {
        if (changeParam.isDeprecated()) {
            return li_deprecatedParam(changeParam);
        }
        boolean changeRequired = changeParam.isChangeRequired();
        boolean changeDescription = Optional.ofNullable(changeParam.getDescription()).map(ChangedMetadata::isDifferent).orElse(false);
        Parameter rightParam = changeParam.getNewParameter();
        Parameter leftParam = changeParam.getNewParameter();
        ContainerTag li = li().withText(changeParam.getName() + " in " + changeParam.getIn());
        if (changeRequired) {
            li.withText(" change into " + (rightParam.getRequired() ? "required" : "not required"));
        }
        if (changeDescription) {
            li
                .withText(" Notes ")
                .with(del(leftParam.getDescription()).withClass("comment"))
                .withText(" change into ")
                .with(span(rightParam.getDescription()).withClass("comment"));
        }
        return li;
    }
}
