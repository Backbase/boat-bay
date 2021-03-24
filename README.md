# BoatBay

---

## Using and Running Boat Bay

### Intro

Boat bay allows for the management and linting of products' specs. This documentation will explain how to
configure the application and how to use it effectively.

#### Scope

The scope of the project is to provide an API Quality Portal that provides a team an overview on the quality on their API.
It allows teams to shift left by using a centralized rules repository for linting specs and maintaining backwards compatability in the pipelines.

### Bootstrap file

Start by creating a bootstrap.yaml file. This will
most likely be located in the same place as the specifications
that are to be used with boat bay.

Your file should look like the example below:

```yaml
portals:
  - key: "repo"
    name: "repo.backbase.com"
    lintRules:
      - ruleId: 150
      - ruleId: B001
      - ruleId: B002
      - ruleId: B003
      - ruleId: B004
      - ruleId: B005
      - ruleId: B006
      - ruleId: B007
      - ruleId: B008
      - ruleId: B009
      - ruleId: B010
      - ruleId: M0012

  - key: "artifacts"
    name: "artifacts.backbase.com"
    lintRules:
      - ruleId: 150
      - ruleId: B001
        severity: SHOULD
      - ruleId: B002
      - ruleId: B003
      - ruleId: B004
      - ruleId: B005
      - ruleId: B006
      - ruleId: B007
      - ruleId: B008
      - ruleId: B009
      - ruleId: B010
      - ruleId: M0012

sources:
  - name: artifacts.backbase.com - Digital Banking
    baseUrl: https://artifacts.backbase.com/staging
    type: JFROG
    active: true
    filterArtifactsName: "banking-services-bom-*-api.zip"
    filterArtifactsCreatedSince: 2020-01-01
    sourcePaths:
      - name:  /com/backbase/dbs/banking-services-bom/
    username: {username}
    password: {password}
    cronExpression: 0 0 13 * * *
    capabilityKeySpEL: sourcePath.substring(0, sourcePath.indexOf('/'))
    capabilityNameSpEL: sourcePath.substring(0, sourcePath.indexOf('/'))
    serviceKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '').replaceAll('-integration([-a-z]*)', '')
    serviceNameSpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '').replaceAll('-integration([-a-z]*)', '')
    specKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-'))
    specFilterSpEL: sourcePath.contains('common-types') || sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('integration') ||  sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('persistence')
    productReleaseKeySpEL: info.name.replaceAll('-api.zip', '')
    productReleaseVersionSpEL: info.name.replaceAll('banking-services-bom-', '').replaceAll('-api.zip', '')
    productReleaseNameSpEL: info.name.replaceAll('-api.zip', '')
    overwriteChanges: false
    runOnStartup: true
    itemLimit: 5
    portal:
      key: "artifacts"
    product:
      key: "digital-banking"
      name: "Digital Banking"
      jiraProjectId: 10235
  - name: repo.backbase.com - Digital Banking
    baseUrl: https://repo.backbase.com/specs
    type: JFROG
    active: true
    filterArtifactsName: "*.yaml"
    sourcePaths:
      - name: /account-statement/
      - name: /action/
      - name: /approval/
      - name: /arrangement-manager/
      - name: /billpay-integrator/
      - name: /budget-planner/
      - name: /card-manager/
      - name: /cash-flow-forecasting
      - name: /cash-management/
      - name: /consent/
      - name: /contact-manager/
      - name: /employee/
      - name: /financial-institution-manager/
      - name: /limit/
      - name: /message/
      - name: /payment/
      - name: /place-manager/
      - name: /portfolio-summary/
      - name: /saving-goal-planner/
      - name: /stop-checks/
      - name: /tradingfx/
      - name: /transaction-category-collector/
      - name: /transaction-manager/
    username: {username}
    password: {password}
    cronExpression: 0 0 12 * * *
    capabilityKeySpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    capabilityNameSpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    serviceKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '').replaceAll('-integration([-a-z]*)', '')
    serviceNameSpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '').replaceAll('-integration([-a-z]*)', '')
    specKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-'))
    specFilterSpEL: sourcePath.contains('common-types') || sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('integration') ||  sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('persistence')
    overwriteChanges: false
    runOnStartup: true
    portal:
      key: "repo"
    product:
      key: "digital-banking"
      name: "Digital Banking"
      jiraProjectId: 10235
  - name: repo.backbase.com - Digital Sales
    baseUrl: https://repo.backbase.com/specs
    type: JFROG
    active: true
    filterArtifactsName: "*.yaml"
    sourcePaths:
      - name: /flow/
    username: {username}
    password: {password}
    cronExpression: 0 5 12 * * *
    capabilityKeySpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    capabilityNameSpEL: sourcePath.substring(1, sourcePath.indexOf('/', 1))
    serviceKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')
    serviceNameSpEL: sourceName.substring(0, sourceName.lastIndexOf('-')).replaceAll('-([a-z]*?)-api', '')
    specFilterSpEL: "sourcePath.contains('common-types') || sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('integration') ||  sourcePath.substring(1, sourcePath.indexOf('/', 1)).contains('persistence')"
    specKeySpEL: sourceName.substring(0, sourceName.lastIndexOf('-'))
    overwriteChanges: false
    runOnStartup: true
    portal:
      key: "repo"
    product:
      key: "digital-sales"
      name: "Digital Sales"
  - name: repo.backbase.com - Platform
    baseUrl: https://repo.backbase.com/specs
    type: JFROG
    active: true
    filterArtifactsName: "*.yaml"
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
    username: {username}
    password: {password}
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
      key: "repo"
    product:
      key: platform
      name: "Platform"
      jiraProjectId: 10235

dashboard:
  name: OpenAPI Quality Dashboard
  defaultPortal:
    key: "repo"

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
- cronExpression: schedule for scanning the source for updates and changes
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

### Building

Run clean install in boat-bay-api project first, a bundled version of the spec needs to be generated.  
Run clean install in boat-bay-server, the models ad interfaces must be generated from the bundled spec.  
Run clean install in boat-bay-client and angular to generate their code.

### Running Locally

To run the application it is fairly simple. First add the following parameters to your run configuration :

- name: backbase.bootstrap.file value: location of the bootstrap.yaml file described above
- name: boat.scheduler.source.scanner.enabled value: true

To run the frontend go to the package.json file and run this line: ` "start": "npm run webpack:dev"`

### Using

#### Uploading a spec

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

---

# BoatBay-jhipster

This application was generated using JHipster 6.10.5, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v6.10.5](https://www.jhipster.tech/documentation-archive/v6.10.5).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](boat-bay-server/package.json).

```
npm install
```

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```

./mvnw


npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](boat-bay-server/package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is commented out by default. To enable it, uncomment the following code in `src/main/webapp/index.html`:

```html
<script>
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./service-worker.js').then(function () {
      console.log('Service Worker Registered');
    });
  }
</script>
```

Note: [Workbox](https://developers.google.com/web/tools/workbox/) powers JHipster's service worker. It dynamically generates the `service-worker.js` file.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/vendor.ts](boat-bay-server/src/main/webapp/app/vendor.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](boat-bay-server/src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

## Building for production

### Packaging as jar

To build the final jar and optimize the BoatBay application for production, run:

```

./mvnw -Pprod clean verify


```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```

java -jar target/*.jar


```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```

./mvnw -Pprod,war clean verify


```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](boat-bay-server/src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

```
docker-compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mysql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 6.10.5 archive]: https://www.jhipster.tech/documentation-archive/v6.10.5
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v6.10.5/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v6.10.5/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v6.10.5/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v6.10.5/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v6.10.5/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v6.10.5/setting-up-ci/
[node.js]: https://nodejs.org/
[yarn]: https://yarnpkg.org/
[webpack]: https://webpack.github.io/
[angular cli]: https://cli.angular.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[jasmine]: https://jasmine.github.io/2.0/introduction.html
[protractor]: https://angular.github.io/protractor/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
