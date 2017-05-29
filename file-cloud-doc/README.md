# file-cloud-doc

documentation of the file cloud project can be edited with swagger-editor e.g. http://editor.swagger.io/#/ 
(just copy-paste content of the swagger.yaml or swagger.json)

or viewed with local instance of swagger-ui (preferred)

to download swagger-ui and inject project documentation into webpage run
```bash
./gradlew printSwaggerUIUrl
```

it prints the static web page url on the standard ouptut which can be copy-pasted into webbrowser, example ouptut shown below:
(cloning swagger ui can be a little bit lengthy)

```bash
 ./gradlew printSwaggerUIUrl
Parallel execution is an incubating feature.
:createBuildDir
:createSwaggerDir
:cloneSwaggerUI UP-TO-DATE
:swaggerUIcopyDist UP-TO-DATE
:swaggerUIcreateIndexHtmlCopy UP-TO-DATE
:printSwaggerUIUrl
open the follwing uri in your web browser: file:/home/mwypych/Dokumenty/praca/dydaktyka/java/java2016/file-cloud-doc/build/swagger/swagger-ui/dist-build/index.html

BUILD SUCCESSFUL

Total time: 1.824 secs

```

One can try to run task which automates opening the generated url in default web browser:
```bash
./gradlew runSwaggerUI
```
but it is working only within unix like environment (requires method xdg-open)
