FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne-billing-rating-mms/config && mkdir /opt/mcb/mvne-billing-rating-mms/log
ADD mvne-billing/mvne-billing-rating-mms/target/mvne-billing-rating-mms.jar /opt/mcb/mvne-billing-rating-mms/
ADD mvne-billing/mvne-billing-rating-mms/startup.sh   /opt/mcb/mvne-billing-rating-mms/
COPY mvne-billing/mvne-billing-rating-mms /opt/mcb/mvne-billing-rating-mms/
RUN chmod +x /opt/mcb/mvne-billing-rating-mms/startup.sh
ENV APPLOGDIR /opt/mcb/mvne-billing-rating-mms/log
ENV APPCONFDIR /opt/mcb/mvne-billing-rating-mms/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne-billing-rating-mms/
CMD ./startup.sh
