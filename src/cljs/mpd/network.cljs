(ns mpd.network
  (:require [mpd.shared :refer [log]]
            [mpd.bullets :as bullets]))

(def websocket (atom nil))
(def syncedFramePlayer (atom nil))
(def currentFramePlayer (atom nil))
(def currentAttachments (atom []))
(def server-id (atom -1))
(def player-sync (atom nil))
(def enemies-sync (atom nil))
(def remove-sync (atom []))

(defn parseJSON [x] (.parse (.-JSON js/window) x))
(defn websocket-open [] (log "OPEN"))
(defn websocket-close [] (log "CLOSE"))
(defn websocket-error [e] (log "ERROR" e))

(defn current-frame []
  )

(defn heartbeat []
  (let [current @currentFramePlayer
        synced @syncedFramePlayer]
    (when (or (not= current synced) (not-empty @currentAttachments))
      (.send @websocket
             (.stringify js/JSON
                         (clj->js (merge @currentFramePlayer
                                         {:entities @currentAttachments}))))
      (reset! currentAttachments [])
      (reset! syncedFramePlayer @currentFramePlayer))))

(defn receive [m]
  (let [data (.-data m)
        json (.parse js/JSON data)]
    (when (= "character" (.-type json))
      (if (= @server-id (.-id json))
        (reset! player-sync json)
        (reset! enemies-sync json)))
    (when (= "remove" (.-type json))
        (swap! remove-sync conj (.-id json)))))

(defn websocket-handshake [m]
  (let [data (.-data m)
        json (.parse js/JSON data)
        player-id (.-id json)]
    (log "HANDSHAKE " player-id)
    (js/setInterval heartbeat 33)
    (reset! server-id player-id)
    (aset @websocket "onmessage" receive)))

(defn attach [entity]
  (swap! currentAttachments conj entity))

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
    (when-not (= (:id (:player @state)) @server-id)
      (log "change player id: " (:id (:player state)) " to " @server-id)
      (swap! state assoc-in [:player :id] @server-id))

    (when-not (nil? @enemies-sync)
      (let [data (js->clj @enemies-sync :keywordize-keys true)
            bullets (:entities data)]
        (when (> (count bullets) 0) (doseq [e bullets] (bullets/fire e)))
        (swap! state assoc :enemies
               (conj (:enemies @state) {(:id data) data}))
        (reset! enemies-sync nil)))

    (when (not-empty @remove-sync)
      (doseq [id @remove-sync]
        (swap! state assoc :enemies
          (dissoc (:enemies @state) id)))
      (reset! remove-sync []))

    (reset! currentFramePlayer (:player @state))
    state))
