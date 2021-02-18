# LabKey Response Module - Implements the Response Server Functionality of the MyStudies System

This custom module provides the Response Server functionality and services, including:

- Processing and storing all mobile app survey and active task responses
- Issuing enrollment tokens to research organizations
- Enrolling and unenrolling participants
- Creating database schemas that match each study's design and updating them as studies change
- Limited querying of data by the mobile app
- Providing web analytics, querying, reporting, and visualizations through manual and programmatic methods

This module must be deployed within the LabKey Server platform (version 21.3.x). To build a standalone distribution of the Response Server (i.e., a standard LabKey Server distribution plus the mobileAppStudy module), follow these steps:

1. Checkout the LabKey Server 21.3.x public GitHub repositories: [Set Up a Development Machine](https://www.labkey.org/Documentation/wiki-page.view?name=devMachine)
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

1. Look in the directory "dist/response" for a file whose name ends with "response-bin.tar.gz". Install this distribution using the [Install LabKey Manually](https://www.labkey.org/Documentation/wiki-page.view?name=manualInstall) instructions.
