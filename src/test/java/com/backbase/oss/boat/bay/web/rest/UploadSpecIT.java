package com.backbase.oss.boat.bay.web.rest;

import com.backbase.oss.boat.bay.BoatBayApp;
import com.backbase.oss.boat.bay.domain.Source;
import com.backbase.oss.boat.bay.repository.SpecRepository;
import com.backbase.oss.boat.loader.OpenAPILoader;
import com.backbase.oss.boat.loader.OpenAPILoaderException;
import io.swagger.v3.oas.models.OpenAPI;
import liquibase.pro.packaged.F;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.bouncycastle.util.Arrays;

import javax.persistence.EntityManager;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BoatBayApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UploadSpecIT {
    @Autowired
    private MockMvc restSpecMockMvc;
    @Autowired
    private SpecRepository specRepository;

    @Mock
    private SpecRepository specRepositoryMock;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    public void uploadSpecEndpointTest() throws Exception {

        String path = "/Users/sophiej/Documents/Projects/opensauce/boat-bay/boat-bay/test-target/exporter/repo.backbase.com/digital-banking/account-statement/account-statement/account-statement-client-api-2.0.0/account-statement-client-api-v2.0.0.yaml";
        OpenAPI openAPI = OpenAPILoader.load(new File(path));
        String productName = "Product";
        String portalName = "Test";
        String portalKey = "test-key";
        String productRelease = "latest";
        String specName = path.split("/")[0].replace("/","");
        String specKey = specName.replaceAll("\\/&-v\\d\\.\\d\\.\\d\\.yaml","");
        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        String request = requestBuild(openAPI,path,specName,specKey,productName, portalName, portalKey);

        restSpecMockMvc.perform(put("api/boat-maven-plugin/upload" )
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.createByteArray(request.length()+1,request)))
            .andExpect(status().isCreated());

    }


    private String requestBuild(OpenAPI openAPI, String path, String name, String key, String productname, String portalName,String portalKey){
        String request = "{" +
            "\"key\": \""+ key+"\"," +
            "\"name\": \"" + name+ "\"," +
            "\"openApi\": \""+openAPI.toString()+"\"," +
            "\"filename\": \""+name+"\"," +
            "\"sourcePath\": \""+path+"\"," +
            "\"portal\": {" +
            "\"name\": \""+portalName+"\"" +
            "\"key\": \""+portalKey+"\"" +
            "}," +
            "\"product\": {" +
            "\"name\": \""+productname+"\"," +
            "\"key\": \""+productname.toLowerCase().replace(" ","-")+"\"," +
            "}," +
            "\"source\": {" +
            "\"name\": \""+path+"\"," +
            "\"portal\": {" +
            "\"name\": \""+portalName+"\"" +
            "\"key\": \""+portalKey+"\"" +
            "}," +
            "\"product\": {" +
            "\"name\": \""+productname+"\"," +
            "\"key\": \""+productname.toLowerCase().replace(" ","-")+"\"," +
            "}"+
            "}"+
            "}";
        return request;
    }

    private String api(){
        String api = "openapi: 3.0.3\n" +
            "info:\n" +
            "  title: Rendition Client API\n" +
            "  version: 1.0.0\n" +
            "servers:\n" +
            "  - url: 'http://localhost:4010'\n" +
            "    description: Prism mock server\n" +
            "tags:\n" +
            "  - name: rendition service\n" +
            "paths:\n" +
            "  '/content/{renditionId}':\n" +
            "    summary: Content stream by rendition ID or stream id\n" +
            "    description: 'Gets content stream of a document rendition by the rendition ID, or by it''s  stream id, in case id begins with \"sid\"'\n" +
            "    get:\n" +
            "      tags:\n" +
            "        - client-api\n" +
            "      summary: 'Gets the content stream of the specified rendition, no rendition properties.'\n" +
            "      description: 'Gets the content stream of the specified rendition, no rendition properties.'\n" +
            "      operationId: renderContent\n" +
            "      parameters:\n" +
            "        - $ref: '#/components/parameters/renditionId'\n" +
            "      responses:\n" +
            "        '200':\n" +
            "          description: Content stream data.\n" +
            "          content:\n" +
            "            '*/*':\n" +
            "              schema:\n" +
            "                type: string\n" +
            "                format: binary\n" +
            "        '400':\n" +
            "          description: Could not read content stream.\n" +
            "        '404':\n" +
            "          description: The rendition is not found.\n" +
            "components:\n" +
            "  parameters:\n" +
            "    renditionId:\n" +
            "      name: renditionId\n" +
            "      in: path\n" +
            "      description: renditionId\n" +
            "      required: true\n" +
            "      schema:\n" +
            "        type: string\n";
        return api;
    }

    @Test
    public void encode(){
        System.out.println(TestUtil.createByteArray(api().length()+1,api()));
    }
}
