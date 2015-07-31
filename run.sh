#!/bin/bash
cd /src
LEIN_ROOT=true /lein run -dev &
cd ./server
bundle exec ruby daemon.rb
