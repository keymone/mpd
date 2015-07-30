(ns mpd.enemies
  (:require [mpd.shared :refer [log]]))

(def enemies (atom {}))

(defn sync [json]
  (swap! enemies conj {(.-id json) json}))

(defn setup []
  (log "  enemies")
  (fn [state]
    (log @enemies)
    (swap! state assoc :enemies @enemies)
    state))
