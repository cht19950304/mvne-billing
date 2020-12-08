FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne-billing-user-analysis/config && mkdir /opt/mcb/mvne-billing-user-analysis/log
ADD mvne-billing/mvne-billing-user-analysis/target/mvne-billing-user-analysis.jar /opt/mcb/mvne-billing-user-analysis/
ADD mvne-billing/mvne-billing-user-analysis/startup.sh   /opt/mcb/mvne-billing-user-analysis/
COPY mvne-billing/mvne-billing-user-analysis /opt/mcb/mvne-billing-user-analysis/
RUN chmod +x /opt/mcb/mvne-billing-user-analysis/startup.sh
ENV APPLOGDIR /opt/mcb/mvne-billing-user-analysis/log
ENV APPCONFDIR /opt/mcb/mvne-billing-user-analysis/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne-billing-user-analysis/
CMD ./startup.sh
