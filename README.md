# Boat Bay

---

## Using and Running Boat Bay

### Intro

Boat bay allows for the management of products specs. This documentation will explain how to
configure the application and how to use it effectively.
### Bootstrap File

The bootstrap file loads the configured sources when you run the application. It specifies the following:
- The sources your specs come from.
- How to search the sources for specs.
- How to process the specs.
- Portal definitions.
- Portal rule configurations.

Start by creating a `bootstrap.yaml` file. This will
most likely be located in a with the specifications
that are to be used with Boat Bay.

The following example shows a typical `bootstrap.yaml` configuration, with definitions for `portals`, `sources`, `dashboard` 
and `specTypes`:
```yaml
portals:
  - key: "repo"
    name: "repo.example.com"
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
sources:
  - name: repo.example.com - PetStore
    baseUrl: https://repo.example.com/
    type: FILESYSTEM
    active: true
    filterArtifactsName: "*.yaml"
    sourcePaths:
      - name:  /petstore/
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
      key: "repo"
    product:
      key: "pet-store"
      name: "Pet Store"
      jiraProjectId: 23098
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
Where `{username}` and `{password}` are you should use your own.

The fields in the source section are as follows:

  - `name`:
        the name of your source
  - `baseUrl`:
        the base url that will be used for your specs
  - `type`:
        this describes where your source in coming from, file system, jfrog, boat maven plugin
  - `active`:
        boolean indicating state of activity
  - `filterArtifactsName`:
        this defines what type of file ending, yaml, json, yml, however it could be made more specific
  -`sourcePaths`:
        this is a list of the parent directories for each capability under the product defined in this source
  - `name`:
        /{capability_name}/
  - `username`:
        username of user accessing repo, these may change depending on the repo configured for this source
  - `password`:
        password of user accessing repo, 
        this will be what ever password/key (usually encrypted) you use to access the repo configured in this source
  - `cronExpression`:
        schedule for scanning the source for updates and changes
  - `capabilityKeySpEL`:
        this is the spring expression language that will be used to
        format/produce the keys for capabilities, recommend use of this example's expressions
  - `capabilityNameSpEL`:
        spring expression language that is used to generate the capability names
  - `serviceKeySpEL`:
        spring expression language that is used to generate the service keys
  - `serviceNameSpEL`:
        spring expression language that is used to generate the service names
  - `specFilterSpEL`:
        spring expression language for filtering specifications
  - `specKeySpEL`:
        spring expression language for generating the spec keys
  - `overwriteChanges`:
        boolean, set this to true to overwrite a spec that is existing if false a new spec is created
  - `runOnStartup`:
        boolean, set this to true to scan specs from this source upon starting the application
  - `portal`:
       - `key`:
            key for this source's portal
       - `name`:
            name for this source's portal
  - `product`
       - `key`:
            key for this product
       - `name`:
            name for this product

Define the `portals` used in the sources where indicated in the example.

### Building

Run mvn clean install in project root.

### Running Locally

To run the application it is fairly simple. First add the following parameters to your run configuration :

- name: `backbase.bootstrap.file` value: location of the bootstrap.yaml file described in [Bootstrap File](#bootstrap-file).
- name: `boat.scheduler.source.scanner.enabled value`: true

To run the frontend  first run the command `npm install` go to the [package.json](boat-bay-server/package.json) file to 
then see the available npm life cycle scripts for the project and run the command labeled start: `npm run webpack:dev` 

#### Resetting Database

Add the following properties to your run configuration:
- spring.liquibase.clear-checksums
- spring.liquibase.drop-first

Set both to true and select them during run when you wish to reset the database.

### Using

#### Uploading a Spec

A spec can be uploaded through [boat-maven-plugin](https://github.com/Backbase/backbase-openapi-tools).  
Before a spec can be uploaded however a source must be set-up. This
can be done as shown in the [Bootstrap](#bootstrap-file) file section.
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
                            <boatBayServerUrl>${url}</boatBayServerUrl>
                            <inputSpec>${project.basedir}/src/main/resources/petstore.yaml</inputSpec>
                            <output>${project.basedir}/src/main/resources/output</output>
                            <sourceKey>sourceKey</sourceKey>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```
For the spec to be uploaded to Boat Bay configuration of the Boat Bay's server url is required.
This URL can be configured in the plugin as show above or omitted from this configuration and instead set as an
environment variable `BOAT_BAY_SERVER_URL`. To configure the URL as an environment variable run `export BOAT_BAY_SERVER_URL={boat-bay-url}`.
There are two options for the URL value, to run locally `http://localhost:8080`  which should only be used for testing,
and `https://boat-bay.proto.backbasecloud.com/`.
The url must be configured in some way for the specs to be uploaded by boat-bay, if not then the spec will only be linted. 
The input spec should be the path of the spec or directory containing the specs you wish to upload.
The output is the location the lint reports get written to. 
The sourceKey refers to the source the spec/s should be uploaded to.

The other option is to send the upload request through a http request file like below:

```http request
 ###
POST http://localhost:8080/api/boat-maven-plugin/{sourceKey}/upload
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
## About the Project

### What is Boat Bay

Boat Bay is a software tool that allows you to create portals for viewing Api Quality and Backwards Compatibility. It 
lints specs using a centralized repository of rules, this rules can be configured per portal making it easy for teams to 
customize linting for their purposes. It also creates reports on the quality, highlighting sections of the spec that 
contain violations or could be improved, and a diff reoprt showing the changes between your api versions, making it easy 
for backwards compatibility.

Boat Bay will also be accessible through BOAT as described in [Uploading a spec](#uploading-a-spec) this will be through 
an extended feature of the linting goal. This will allow a developer to upload a spec to Boat Bay through the build process.
You can upload the same spec multiple times, while uploading from a project with a snapshot version the spec will be 
updated, otherwise a new spec will be created.

### Generated code

The base of this project is generated by JHipster, the documentation for which can be found at the end of this readme.
Most of the custom code resides in boat-bay-server, to make it easier to distinguish between generated code and manually
written code this module's src has been split into two directories, [backbase](boat-bay-server/src/backbase) contains 
out custom code, and [main](boat-bay-server/src/backbase) contains generated code.

As well as some base structure and tests being generated, interfaces for controllers, and the models they make use of are
generated from our api specs found in [boat-bay-api](boat-bay-api), this is done using [BOAT maven plugin](https://github.com/Backbase/backbase-openapi-tools). 

---

# Boat-Bay-jhipster

This application was generated using JHipster 6.10.5, you can find documentation and help at
 [https://www.jhipster.tech/documentation-archive/v6.10.5](https://www.jhipster.tech/documentation-archive/v6.10.5).

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

To build the final jar and optimize the Boat Bay application for production, run:

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
