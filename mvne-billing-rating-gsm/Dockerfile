FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne-billing-rating-gsm/config && mkdir /opt/mcb/mvne-billing-rating-gsm/log
ADD mvne-billing/mvne-billing-rating-gsm/target/mvne-billing-rating-gsm.jar /opt/mcb/mvne-billing-rating-gsm/
ADD mvne-billing/mvne-billing-rating-gsm/startup.sh   /opt/mcb/mvne-billing-rating-gsm/
COPY mvne-billing/mvne-billing-rating-gsm /opt/mcb/mvne-billing-rating-gsm/
RUN chmod +x /opt/mcb/mvne-billing-rating-gsm/startup.sh
ENV APPLOGDIR /opt/mcb/mvne-billing-rating-gsm/log
ENV APPCONFDIR /opt/mcb/mvne-billing-rating-gsm/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne-billing-rating-gsm/
CMD ./startup.sh
