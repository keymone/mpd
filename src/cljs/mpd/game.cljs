(ns mpd.game
  (:require [mpd.shared :refer [log]]))

(defn setup [stage player network]
  (log "  game")
  (main-loop stage player network))

(defn main-loop [stage player network]
  (network)
  (player)
  (stage)
  (js/requestAnimationFrame #(main-loop stage player network)))
