FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne-billing-balance-manage/config && mkdir /opt/mcb/mvne-billing-balance-manage/log
ADD mvne-billing/mvne-billing-balance-manage/target/mvne-billing-balance-manage.jar /opt/mcb/mvne-billing-balance-manage/
ADD mvne-billing/mvne-billing-balance-manage/startup.sh   /opt/mcb/mvne-billing-balance-manage/
COPY mvne-billing/mvne-billing-balance-manage /opt/mcb/mvne-billing-balance-manage/
RUN chmod +x /opt/mcb/mvne-billing-balance-manage/startup.sh
ENV APPLOGDIR /opt/mcb/mvne-billing-balance-manage/log
ENV APPCONFDIR /opt/mcb/mvne-billing-balance-manage/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne-billing-balance-manage/
CMD ./startup.sh
