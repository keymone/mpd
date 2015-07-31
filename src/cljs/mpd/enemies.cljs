(ns mpd.enemies
  (:require [mpd.shared :refer [log entity_looper]]
            [mpd.stage :refer [dimensions]]))

(defn setup []
  (log "  enemies")
  (fn [state]
    (let [player (:player @state)
          x (:x player) y (:y player)
          w2 (/ (:w dimensions) 2)
          h2 (/ (:h dimensions) 2)
          enemies (:enemies @state)]
      (doseq [kv (seq enemies)]
        (let [id (first kv)
              enemy (last kv)
              ex (:x enemy) ey (:y enemy)

              off-top (< ey (- y (- h2 20)))
              off-bot (> ey (+ y (- h2 20)))
              off-lft (< ex (- x (- w2 20)))
              off-rgt (> ex (+ x (- w2 20)))]
          ; update off-screen attribute
          (swap! state assoc-in [:enemies id :off-screen]
                 (or off-top off-bot off-rgt off-lft)))))
    state))
