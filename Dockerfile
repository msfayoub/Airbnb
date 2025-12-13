# Use TomEE 8.0.1 Plume with JDK 8 (compatible with Java 8 compiled classes)
FROM tomee:8-jre-8.0.1-plume

# Remove default applications
RUN rm -rf /usr/local/tomee/webapps/*

# Download Prometheus JMX Exporter
ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.20.0/jmx_prometheus_javaagent-0.20.0.jar /usr/local/tomee/lib/jmx_prometheus_javaagent.jar

# Create JMX exporter config
RUN echo "---\n\
rules:\n\
  - pattern: '.*'\n\
" > /usr/local/tomee/conf/jmx-exporter-config.yaml

# Copy the WAR file to TomEE's webapps directory
COPY dist/airbnb.war /usr/local/tomee/webapps/ROOT.war

# Expose ports
EXPOSE 8080 9090

# Set CATALINA_OPTS to include JMX exporter
ENV CATALINA_OPTS="-javaagent:/usr/local/tomee/lib/jmx_prometheus_javaagent.jar=9090:/usr/local/tomee/conf/jmx-exporter-config.yaml"

# Start TomEE
CMD ["catalina.sh", "run"]
