(ns mpd.enemies
  (:require [mpd.shared :refer [log]]))

(def enemies (atom {}))

(defn sync [json]
  (swap! enemies conj {(.-id json) (js->clj json :keywordize-keys true)}))

(defn setup []
  (log "  enemies")
  (fn [state]
    (swap! state assoc :enemies @enemies)
    state))
