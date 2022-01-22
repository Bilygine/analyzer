### Bilygine Analyzer - Build 0.1
#
FROM openjdk:8
RUN mkdir /analyzer
WORKDIR /analyzer
# Copy jar
COPY target/analyzer-core-*.jar analyzer.jar
# Copy dependencies
COPY target/lib/* lib/
# Copy configuration
# COPY conf/ conf/
# Make port 8080 available outside this container
EXPOSE 8123
# Run analyzer with dependencies
CMD ["/usr/bin/java", "-cp", "analyzer.jar:lib/:conf/", "com.bilygine.analyzer.Analyzer"]
