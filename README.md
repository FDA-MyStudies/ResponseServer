# LabKey Response Module - Implements the Response Server Functionality of the MyStudies System

This custom module provides the Response Server functionality and services, including:

- Processing and storing all mobile app survey and active task responses
- Issuing enrollment tokens to research organizations
- Enrolling and unenrolling participants
- Creating database schemas that match each study's design and updating them as studies change
- Limited querying of data by the mobile app
- Providing web analytics, querying, reporting, and visualizations through manual and programmatic methods

## Evaluation Setup Instructions

This module can be used to create a standalone distribution of the MyStudies Response Server.
_(The following commands and paths are relative to your `Response` enlistment)_

1. Install JDK 14+
   - [AdoptOpenJDK](https://adoptopenjdk.net/releases.html?variant=openjdk14&jvmVariant=hotspot)
   - Your `JAVA_HOME` environment variable should point at a compatible JDK install
1. Create 'Response' LabKey distribution
   - (Linux/MacOS) `./gradlew -I init.gradle -PdeployMode=prod :distributions:fda:distribution`
   - (Windows) `.\gradlew -I init.gradle -PdeployMode=prod :distributions:fda:distribution`
1. Locate distribution archive
   - (Linux/MacOS) `dist/response/LabKey*-response.tar.gz`
   - (Windows) `dist\response\LabKey*-response.tar.gz`
1. Follow [instructions for manual deployment](https://www.labkey.org/Documentation/wiki-page.view?name=manualInstall) of the distribution archive

## Developer Setup Instructions

This module can be developed within the LabKey Server platform (version 21.3.x). To setup a development environment for the Response Server (i.e. a standard LabKey Server distribution plus the Response module), follow these steps:

1. Checkout the LabKey Server 21.3.x public GitHub repositories: [Set Up a Development Machine](https://www.labkey.org/Documentation/wiki-page.view?name=devMachine)

1. Clone the Response module (this repository) into `server/modules`

1. Navigate to the root of your LabKey enlistment.

1. Append these two lines to the end of `settings.gradle`:
   ```
   include ":server:modules:Response"
   include ":server:modules:Response:distributions:fda"
   ```

1. On the command line (again, in the root of your working copy), run one of these commands (use the first command on Linux/OSX and the second on Windows):

    ```
    ./gradlew :server:modules:Response:distributions:fda:dist
    gradlew :server:modules:Response:distributions:fda:dist
    ```

1. [Build and deploy LabKey](https://www.labkey.org/Documentation/wiki-page.view?name=buildLabKey) with the Response module.
