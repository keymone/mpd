FROM ubuntu:14.04
ENV DEBIAN_FRONTEND=noninteractive
ENV LEIN_ROOT=true

RUN apt-get -y install software-properties-common && add-apt-repository ppa:openjdk-r/ppa
RUN apt-get update && apt-get -y install openjdk-8-jdk wget
RUN wget -O /lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein && chmod +x /lein
ADD . /src
RUN sh -c 'cd /src; /lein compile; /lein cljsbuild once'

EXPOSE 8080

CMD /src/run.sh
