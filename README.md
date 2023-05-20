# gwt-cardmaster-maven

The GWT CardMaster web application, maven flavor and JUnit testing.

To test it, run:

`mvn -U -e gwt:codeserver -pl *-client -am`

to execute the codeserver (just keep that running),
then you can use

`mvn -U jetty:run-forked -pl *-server -am -Denv=dev`

to run the application in developer mode (the URL is `http://localhost:8080/`). 
