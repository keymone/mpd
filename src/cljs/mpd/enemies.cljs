(ns mpd.enemies
  (:require [mpd.shared :refer [log]]))

(defn setup []
  (log "  enemies")
  (fn [state] state))
