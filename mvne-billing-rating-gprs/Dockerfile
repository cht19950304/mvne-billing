FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne-billing-rating-gprs/config && mkdir /opt/mcb/mvne-billing-rating-gprs/log
ADD mvne-billing/mvne-billing-rating-gprs/target/mvne-billing-rating-gprs.jar /opt/mcb/mvne-billing-rating-gprs/
ADD mvne-billing/mvne-billing-rating-gprs/startup.sh   /opt/mcb/mvne-billing-rating-gprs/
COPY mvne-billing/mvne-billing-rating-gprs /opt/mcb/mvne-billing-rating-gprs/
RUN chmod +x /opt/mcb/mvne-billing-rating-gprs/startup.sh
ENV APPLOGDIR /opt/mcb/mvne-billing-rating-gprs/log
ENV APPCONFDIR /opt/mcb/mvne-billing-rating-gprs/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne-billing-rating-gprs/
CMD ./startup.sh
