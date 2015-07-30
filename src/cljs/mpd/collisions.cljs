(ns mpd.collisions
  (:require [mpd.shared :refer [log]]))

(defn setup []
  (log "  collisions")
  (fn [state]
    ; for each player
    ;   for each bullet
    ;     check distance p <-> b
    ;(doseq [player (conj (:enemies @state) (:player state))
    ;        bullet (:bullets @state)]
    ;  (when (< (distance player bullet) 50)
    ;    (if (= player (:player state))
    ;      (swap! state assoc-in [:player :hp]
    ;             (- (:hp (:player @state)) (:damage bullet)))
    ;      (swap! state assoc-in [:enemies (:id player)]
    ;             (- (:hp (get (:enemies @state) (:id player))) (:damage bullet))))
    ;    (swap! state assoc :bullets
    ;           (remove #(= % bullet) (:bullets @state)))))
    state))
