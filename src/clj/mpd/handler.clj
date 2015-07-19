(ns mpd.handler
  (:use ring.adapter.jetty))

(defn app [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello"})

(defn -main [& args]
  (run-jetty app {:port 8080}))
