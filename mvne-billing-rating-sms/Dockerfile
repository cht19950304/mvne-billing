FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne-billing-rating-sms/config && mkdir /opt/mcb/mvne-billing-rating-sms/log
ADD mvne-billing/mvne-billing-rating-sms/target/mvne-billing-rating-sms.jar /opt/mcb/mvne-billing-rating-sms/
ADD mvne-billing/mvne-billing-rating-sms/startup.sh   /opt/mcb/mvne-billing-rating-sms/
COPY mvne-billing/mvne-billing-rating-sms /opt/mcb/mvne-billing-rating-sms/
RUN chmod +x /opt/mcb/mvne-billing-rating-sms/startup.sh
ENV APPLOGDIR /opt/mcb/mvne-billing-rating-sms/log
ENV APPCONFDIR /opt/mcb/mvne-billing-rating-sms/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne-billing-rating-sms/
CMD ./startup.sh
