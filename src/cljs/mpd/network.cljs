(ns mpd.network
  (:require [mpd.shared :refer [log]]))

(def websocket (atom nil))
(def lastFramePlayer (atom nil))
(def network-state (atom :initial))
(def inbox (atom []))

(defn parseJSON [x] (.parse (.-JSON js/window) x))
(defn websocket-open [] (log "OPEN"))
(defn websocket-close [] (log "CLOSE"))
(defn websocket-error [e] (log "ERROR" e))

(defn websocket-handshake [m]
  (log "HANDSHAKE" m)
  (aset @websocket "onmessage" receive)
  (reset! network-state :connected))

(defn send [m]
  (when (= @network-state :connected)
    (log " player data sent to server")
    (.send @websocket m)))

(defn receive [m]
  (let [data (.-data m)]
    (log "data received:" m)
    (swap! inbox conj data)))

(defn clj->json [ds]
  (.stringify js/JSON (clj->js ds)))

(defn setup []
  (log "  network")
  (log "connecting...")

  (let [ws (js/WebSocket. "ws://localhost:8197")]
    (doall (map #(aset ws (first %) (second %))
           [["onopen" websocket-open]
            ["onclose" websocket-close]
            ["onerror" websocket-error]
            ["onmessage" websocket-handshake]]))
    (reset! websocket ws)
    (log "Websocket setup done"))

  (fn [state]
    ; erase inbox queue for now
    (reset! inbox [])
    (let [player (:player @state)]
      (when (not= player @lastFramePlayer)
        (send  (clj->json player)))
      (reset! lastFramePlayer player))
    state))
