FROM paasRegistry:5000/library/centos7_jdk1.8:jdk1.8
MAINTAINER chenjk chenjk@chinamobilesz.com
RUN mkdir -p /opt/mcb/mvne_multiple_collect/main/config && mkdir /opt/mcb/mvne_multiple_collect/log && mkdir /opt/mcb/mvne_multiple_collect/data && mkdir /opt/mcb/mvne_multiple_collect/data/localincoming && mkdir /opt/mcb/mvne_multiple_collect/fileVal && mkdir /opt/mcb/mvne_multiple_collect/fileVal/RightFileVal && mkdir /opt/mcb/mvne_multiple_collect/fileVal/ErrorFileVal && mkdir /opt/mcb/mvne_multiple_collect/decode && mkdir /opt/mcb/mvne_multiple_collect/decode/decode && mkdir /opt/mcb/mvne_multiple_collect/sort && mkdir /opt/mcb/mvne_multiple_collect/sort/DupSortCdr && mkdir /opt/mcb/mvne_multiple_collect/dup && mkdir /opt/mcb/mvne_multiple_collect/dup/NodupPath && mkdir /opt/mcb/mvne_multiple_collect/dup/dupPath && mkdir /opt/mcb/mvne_multiple_collect/cdr && mkdir /opt/mcb/mvne_multiple_collect/cdr/RightCdrVal && mkdir /opt/mcb/mvne_multiple_collect/cdr/ErrorCdrVal && mkdir /opt/mcb/mvne_multiple_collect/data/localBak && mkdir /opt/mcb/mvne_multiple_collect/fileVal/fileValBak && mkdir /opt/mcb/mvne_multiple_collect/decode/decodeBak && mkdir /opt/mcb/mvne_multiple_collect/sort/sortBak && mkdir /opt/mcb/mvne_multiple_collect/dup/dupBak && mkdir /opt/mcb/mvne_multiple_collect/cdr/cdrBak
ADD mvne-multiple-collect/target/mvne-multiple-collect.jar /opt/mcb/mvne_multiple_collect/main/
ADD mvne-multiple-collect/startup.sh   /opt/mcb/mvne_multiple_collect/main/
COPY mvne-multiple-collect /opt/mcb/mvne_multiple_collect/
RUN chmod +x /opt/mcb/mvne_multiple_collect/main/startup.sh
ENV APPLOGDIR /opt/mcb/mvne_multiple_collect/log
ENV APPCONFDIR /opt/mcb/mvne_multiple_collect/main/config/application.properties
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
WORKDIR /opt/mcb/mvne_multiple_collect/main/
CMD ./startup.sh