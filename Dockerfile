FROM ubuntu:14.04
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get -y install software-properties-common
RUN add-apt-repository ppa:openjdk-r/ppa
RUN apt-get update
RUN apt-get -y install openjdk-8-jdk ruby
RUN apt-get -y remove ruby
RUN add-apt-repository ppa:brightbox/ruby-ng-experimental
RUN apt-get update
RUN apt-get -y install ruby2.2 ruby-dev wget build-essential libstdc++6 openssl
RUN wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
RUN chmod +x /lein
ADD . /src
RUN gem install bundler
RUN sh -c 'cd /src/server; bundle install --path .bundle'
RUN sh -c 'cd /src; LEIN_ROOT=true /lein cljsbuild once'

EXPOSE 8080
EXPOSE 8917

CMD /src/run.sh
