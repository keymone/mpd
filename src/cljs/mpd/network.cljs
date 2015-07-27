(ns mpd.network
  (:require [mpd.shared :refer [log]]))

(defn parseJSON [x]
  (.parse (.-JSON js/window) x))

(def websocket* (atom []))

(defn- send [m]
  (.send @websocket* m))

(defn- receive [m]
  (log (.-data m)))

(defn setup []
  (log "  network")
  (log "connecting...")
  (reset! websocket* (js/WebSocket. "ws://localhost:8192"))
  (doall
      (map #(aset @websocket* (first %) (second %))
           [["onopen" (fn [] (log "OPEN"))]
            ["onclose" (fn [] (log "CLOSE"))]
            ["onerror" (fn [e] (log (str "ERROR:" e)))]
            ["onmessage" (fn [m] (receive m) )]]))

  (log "connected.")

  (fn [state] state))
