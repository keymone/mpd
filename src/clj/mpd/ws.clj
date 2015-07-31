(ns mpd.ws
  (:use org.httpkit.server
        clojure.tools.logging)
  (:require [clojure.data.json :as json]))

(defonce clients (atom {}))
(defonce players (atom {}))
(defonce player_counter (atom 0))

(defn closed [id channel status]
  (swap! clients dissoc id)
  (swap! players dissoc id)
  (doseq [other (vals @clients)]
    (send! other (json/write-str {:type "remove" :id id})))
  (info channel "closed, status" status))

(defn opened [id channel]
  (info channel " connected as " id)
  (swap! clients assoc id channel)
  (send! channel (json/write-str {:id id}))
  (doseq [player (vals @players)]
    (send! channel player)))

(defn received [id channel msg]
  (swap! players assoc id msg)
  (doseq [other (vals @clients)]
    (when (not= other channel) (send! other msg))))

(defn handle [req]
  (with-channel req channel
    (swap! player_counter inc)
    (let [id @player_counter]
      (opened id channel)
      (on-receive channel #(received id channel %))
      (on-close channel #(closed id channel %)))))
