(ns mpd.network
  (:require [mpd.shared :refer [log]]))

(defn setup []
  (log "  network")
  (fn [state] state))
