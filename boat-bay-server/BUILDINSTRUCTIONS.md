# Using and Running Boat Bay

## Intro

Boat bay allows for the management of products specs. This documentation will explain how to
configure the application and how to use it effectively

## Bootstrap file

Start by creating a bootstrap.yaml file. This will
most likely be located in a with the specifications
that are to be used with boat bay.

Your file should look like the example below:

```yaml
portals:
  - key: 'repo'
    name: 'repo.backbase.com'
  - key: 'maven-plugin-test'
    name: 'com.backbase.oss.boat'
sources:
  - name: repo.backbase.com - Platform
    baseUrl: https://repo.backbase.com/specs
    type: JFROG
    active: true
    filterArtifactsName: '*.yaml'
    sourcePaths:
      - name: /access-control/
      - name: /audit/
      - name: /authorized-user/
      - name: /contentservices/
      - name: /device-management-service/
      - name: /notification/
      - name: /portal/
      - name: /provisioning/
      - name: /push-integration/
      - name: /renditionservice/
      - name: /space-controller/
      - name: /targeting/
      - name: /user-manager/
      - name: /user-profile-manager/
      - name: /versionmanagement-persistence/
    username: 'sophiej'
    password: { pasword }
    cronExpression: 0 10 12 * * *
    capabilityKeySpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    capabilityNameSpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    serviceKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')
    serviceNameSpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')
    specFilterSpEL: "sourcePath.contains('common-types') || sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('integration') ||  sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('persistence')"
    specKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-'))
    overwriteChanges: false
    runOnStartup: true
    portal:
      key: 'repo'
    product:
      key: platform
      name: 'Platform'
  - name: maven.plugin.boat.test - Petstore
    baseUrl: https://repo.backbase.com/specs
    type: BOAT_MAVEN_PLUGIN
    active: true
    filterArtifactsName: '*.yaml'
    sourcePaths:
      - name: /petstore/
    username: 'sophiej'
    password: { password }
    cronExpression: 0 10 12 * * *
    capabilityKeySpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    capabilityNameSpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    serviceKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')
    serviceNameSpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')
    specFilterSpEL: "sourcePath.contains('common-types') || sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('integration') ||  sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('persistence')"
    specKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-'))
    overwriteChanges: false
    runOnStartup: false
    portal:
      key: 'maven-plugin-test'
      name: 'com.backbase.oss.boat'
    product:
      key: 'petstore'
      name: 'Pet Store'
dashboard:
  name: OpenAPI Quality Dashboard
  defaultPortal:
    key: 'repo'

specTypes:
  - name: Client API
    matchSpEL: sourceName.contains('client-api')
    icon: public
  - name: Service API
    matchSpEL: sourceName.contains('service-api')
    icon: public_off
  - name: Integration Inbound
    matchSpEL: sourceName.contains('inbound')
    icon: input
  - name: Integration Outbound
    matchSpEL: sourceName.contains('outbound')
    icon: outbound
  - name: Integration Pull
    matchSpEL: sourceName.contains('pull')
    icon: swap_vert
  - name: Integration
    matchSpEL: sourceName.contains('integration-api')
    icon: import_export
  - name: Persistence
    matchSpEL: sourceName.contains('persistence') || sourceName.contains('pandp')
    icon: storage
```

This is where you can configure and set up product sources.
It should contain definitions for your portals, sources, dashboard and spec types.
Where username and password are you should use your own users.
The example above show the configuration for 2 sources. Break down of the source example:

- name: the name of your source
- baseUrl: the base url that will be used for your specs
- type: This describes where your source in coming from, file system, jfrog, boat maven plugin
- active: boolean indicating state of activity
- filterArtifactsName: this defines what type of file ending, yaml, json, yml, however it could be made more specific
- sourcePaths: this is a list of the parent directories for each capability under the product defined in this source
  - name: /{capability_name}/
- username: username of user
- password: password of user
- cronExpression: ???
- capabilityKeySpEL: this is the spring expression language that will be used to
  format/produce the keys for capabilities, recommend use of this example's expressions
- capabilityNameSpEL: spring expression language that is used to generate the capability names
- serviceKeySpEL: spring expression language that is used to generate the service keys
- serviceNameSpEL: spring expression language that is used to generate the service names
- specFilterSpEL: spring expression language for filtering specifications
- specKeySpEL: spring expression language for generating the spec keys
- overwriteChanges: boolean
- runOnStartup: boolean
- portal:
- key: key for this source's portal
- name: name for this source's portal
- product:
- key: key for this product
- name: name for this product
  Define the portals used in the sources where indicated in the example.

## Building

Run clean install in boat-bay-api project first, a bundled version of the spec needs to be generated.  
Run clean install in boat-bay-server, the models ad interfaces must be generated from the bundled spec.  
Run clean install in boat-bay-client and angular to generate their code.

## Running

To run the application it is fairly simple. First add the following parameters to your run configuration :

- name: backbase.bootstrap.file value: location of the bootstrap.yaml file described above
- name: boat.scheduler.source.scanner.enabled value: true

To run the frontend go to the package.json file and run this line: ` "start": "npm run webpack:dev"`

## Using

### Uploading a spec

A spec can be uploaded through boat-maven-plugin.  
Before a spec can be uploaded however a source must be set-up. This
can be done as shown in the Bootstrap file section.
The plugin configuration will look like example below:

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>com.backbase.oss</groupId>
                <artifactId>boat-maven-plugin</artifactId>
                <executions>
                    <execution>
                        <id>lint</id>
                        <phase>validate</phase>
                        <goals>
                            <goal>lint</goal>
                        </goals>
                        <configuration>
                            <boatBayUrl>${url}</boatBayUrl>
                            <inputSpec>${project.basedir}/src/main/resources/petstore.yaml</inputSpec>
                            <output>${project.basedir}/src/main/resources/output</output>
                            <sourceId>6</sourceId>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

In the plugin configuration you can customise the output path to the location you want
the lint reports to be generated this is also where the specs will be uploaded to, the
boatBayUrl should be boat bay client, and the input spec should be the path of the spec
or directory containing the specs you wish to upload.  
Or through a http request file like below:

```http request
 ###
PUT http://localhost:8080/api/boat-maven-plugin/5/upload
Authorization: Basic YWRtaW46YWRtaW4=
Content-Type: application/json

{
  "specs": [
    {
      "key": "renditionservice-client-api",
      "name": "renditionservice-client-api-v1.2.0.yaml",
      "openApi": "openapi: 3.0.3\r\ninfo:\r\n  title: Rendition Client API\r\n  version: 1.2.0\r\nservers:\r\n  - url: 'http:\/\/localhost:4010'\r\n    description: Prism mock server\r\ntags:\r\n  - name: rendition service\r\npaths:\r\n  '\/content\/{renditionId}':\r\n    summary: Content stream by rendition ID or stream id\r\n    description: 'Gets content stream of a document rendition by the rendition ID, or by it''s  stream id, in case id begins with \"sid\"'\r\n    get:\r\n      tags:\r\n        - client-api\r\n      summary: 'Gets the content stream of the specified rendition, no rendition properties.'\r\n      description: 'Gets the content stream of the specified rendition, no rendition properties.'\r\n      operationId: renderContent\r\n      parameters:\r\n        - $ref: '#\/components\/parameters\/renditionId'\r\n      responses:\r\n        '200':\r\n          description: Content stream data.\r\n          content:\r\n            '*\/*':\r\n              schema:\r\n                type: string\r\n                format: binary\r\n        '400':\r\n          description: Could not read content stream.\r\n        '404':\r\n          description: The rendition is not found.\r\ncomponents:\r\n  parameters:\r\n    renditionId:\r\n      name: renditionId\r\n      in: path\r\n      description: renditionId\r\n      required: true\r\n      schema:\r\n        type: string\r\n",
      "filename": "renditionservice-client-api-v1.2.0.yaml"
    }
  ],
  "location": "/Users/sophiej/Documents/Projects/opensauce/boat-bay/boat-bay-data",
  "projectId": "com.backbase.oss.boat.renditionservice",
  "artifactId": "renditionservice-spec",
  "version": "1.2.0"
}
```

The projectId and artifactId should be that of the project where the specs are coming from.
