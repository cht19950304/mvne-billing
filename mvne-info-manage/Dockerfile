FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne_info_manage/main/config && mkdir /opt/mcb/mvne_info_manage/log

ADD mvne-info-manage/target/mvne-info-manage-0.0.1-SNAPSHOT.jar /opt/mcb/mvne_info_manage/main/
ADD mvne-info-manage/startup.sh   /opt/mcb/mvne_info_manage/main/
COPY mvne-info-manage /opt/mcb/mvne_info_manage/
RUN chmod +x /opt/mcb/mvne_info_manage/main/startup.sh
ENV APPLOGDIR /opt/mcb/mvne_info_manage/log
ENV APPCONFDIR /opt/mcb/mvne_info_manage/main/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne_info_manage/main/
CMD ./startup.sh
