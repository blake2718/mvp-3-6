FROM openjdk:7

RUN mkdir /app
COPY src /app/src
COPY *.jar /app/

WORKDIR /app
RUN javac -cp com.ibm.mq.allclient-9.1.4.0.jar:javax.jms-api-2.0.1.jar:. src/mypack/Main.java

CMD ["java", "-cp", "com.ibm.mq.allclient-9.1.4.0.jar:javax.jms-api-2.0.1.jar:.", "src.mypack.Main"]