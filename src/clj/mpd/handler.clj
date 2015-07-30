(ns mpd.handler
  (:use ring.middleware.resource
        ring.middleware.file-info
        ring.middleware.logger
        ring.middleware.reload
        ring.middleware.file-info)
  (:require [org.httpkit.server :as http-kit]))

(defn wrap-dir-index [handler]
  (fn [req]
    (handler
      (update-in req [:uri]
                 #(if (= "/" %) "/index.html" %)))))

(def app
  (-> (fn [req] {:status 404
                 :headers {"Content-Type" "text/html"}
                 :body "Gone"})
      (wrap-resource "public")
      (wrap-file-info)
      (wrap-dir-index)
      (wrap-with-logger)
      ))

(defn -main [& args]
  (http-kit/run-server app {:port 8080}))
