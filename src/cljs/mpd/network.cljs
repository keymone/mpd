(ns mpd.network
  (:require [mpd.shared :refer [log]]
            [mpd.enemies :as enemies]))

(def websocket (atom nil))
(def syncedFramePlayer (atom nil))
(def currentFramePlayer (atom nil))
(def network-state (atom nil))
(def server-id (atom -1))
(def inbox (atom []))

(defn parseJSON [x] (.parse (.-JSON js/window) x))
(defn websocket-open [] (log "OPEN"))
(defn websocket-close [] (log "CLOSE"))
(defn websocket-error [e] (log "ERROR" e))

(defn websocket-handshake [m]
  (let [data (.-data m)
        json (.parse js/JSON data)
        player-id (.-id json)]
    (log "HANDSHAKE " player-id)
    (js/setInterval heartbeat 33)
    (reset! server-id player-id)
    (aset @websocket "onmessage" receive)
    (reset! network-state :connected)))

(defn current-frame []
  (merge @currentFramePlayer {:entities @currentAttachments}))

(defn heartbeat []
  (let [current @currentFramePlayer
        synced @syncedFramePlayer]
    (when (not= current synced)
      (send (clj->json (current-frame)))
      (reset! currentAttachments [])
      (reset! syncedFramePlayer @currentFramePlayer))))

(defn send [m]
  (when (= @network-state :connected)
    (.send @websocket m)))

(defn receive [m]
  (let [data (.-data m)
        json (.parse js/JSON data)]
    (if (= @server-id (.-id json))
      (player/sync json)
      (enemies/sync json))))

(def currentAttachments (atom []))
(defn attach [entity]
  (swap! currentAttachments conj entity))

(defn clj->json [ds]
  (.stringify js/JSON (clj->js ds)))

(defn setup []
  (log "  network")
  (log "connecting...")

  (let [ws (js/WebSocket. "ws://127.0.0.1:8197")]
    (doall (map #(aset ws (first %) (second %))
           [["onopen" websocket-open]
            ["onclose" websocket-close]
            ["onerror" websocket-error]
            ["onmessage" websocket-handshake]]))
    (reset! websocket ws)
    (log "Websocket setup done"))

  (fn [state]
    (when (not= (:id (:player @state)) @server-id)
      (log "change player id: " (:id (:player state)) " to " @server-id)
      (swap! state assoc-in [:player :id] @server-id))

    (reset! currentFramePlayer (:player @state))
    state))
