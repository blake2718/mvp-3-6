# java-msvc

this repo includes a project config file for intellij. to use that, just import this folder from existing source code in intellij

to run on your machine, run the following commands from the root of this project.

javac -cp com.ibm.mq.allclient-9.1.4.0.jar:javax.jms-api-2.0.1.jar:. src/mypack/Main.java
java -cp com.ibm.mq.allclient-9.1.4.0.jar:javax.jms-api-2.0.1.jar:. src.mypack.Main

to build the image for this microservice, run

docker build .

known problems include not being able to connect to MQ from inside a container. Probably due to not having the proper ports exposed.
