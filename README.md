# DemoService

The project has been adapted torun on SCP Neo Environment.

You can run it locally within Eclipse or by using Maven integration with SCP-SDK 
> look at the pom.xml in the SDK samples for details on how to use it with Maven

Or you can test it directly by deploying it onto SCP-Neo

Remeber that the URL in Neo takes into consideration the application context - which is defined by the war file and will be added as sufix to your application url. Thus, you application would have the following format:

Neo:
```https://<app-name><account>.hanatrial.ondemand.com/<war-name>```
 
Whereas while running it under Eclipse, you would have something like
 
Localhost:
```http://localhost:8080/<web-module-name>```
  
Full URL Examples:

```http://localhost:8080/DemoService/odata/v4/DemoService/People(0)```
```https://coildemoXXXXXXtrial.hanatrial.ondemand.com/COILDemo/odata/v4/DemoService/People(0)```

