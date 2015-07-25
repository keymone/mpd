(ns mpd.game
  (:require [mpd.shared :refer [log]]))

(defn setup [stage player network]
  (log "  game")
  (main-loop stage player network))

(def state (atom {}))

(defn main-loop [stage player network]
  (-> state network player stage)
  (js/requestAnimationFrame #(main-loop stage player network)))
