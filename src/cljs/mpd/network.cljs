(ns mpd.network
  (:require [mpd.shared :refer [log]]))

(defn parseJSON [x]
  (.parse (.-JSON js/window) x))

(def websocket (js/WebSocket. "ws://localhost:8192"))

(def lastFramePlayer (atom nil))

(def inbox (atom []))

(defn clj->json
  [ds]
  (.stringify js/JSON (clj->js ds)))

(defn- send [m]
  (.send websocket m))

(defn- receive [m]
  (let [data (.-data m)]
    (swap! inbox conj data)
    (log data)))

(defn setup []
  (log "  network")
  (log "connecting...")

  (doall
      (map #(aset websocket (first %) (second %))
           [["onopen" (fn [] (log "OPEN"))]
            ["onclose" (fn [] (log "CLOSE"))]
            ["onerror" (fn [e] (log (str "ERROR:" e)))]
            ["onmessage" (fn [m] (receive m) )]]))

  (log "connected.")

  (fn [state]
    ; erase inbox queue for now
    (reset! inbox [])
    (let [player (:player @state)]
      (when (not= player @lastFramePlayer)
        (let [json (clj->json player)]
          (send json)
          (log " player data sent to server")))
      (reset! lastFramePlayer player))
    state))
