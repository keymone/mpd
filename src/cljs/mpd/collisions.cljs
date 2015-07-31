(ns mpd.collisions
  (:require [mpd.shared :refer [log]]))

(defn distance [a b]
  (Math/sqrt (+ (Math.pow (- (:x a) (:x b)) 2)
                (Math.pow (- (:y a) (:y b)) 2))))

(defn setup []
  (log "  collisions")
  (fn [state]
    ; for each player
    ;   for each bullet
    ;     check distance p <-> b
    (doseq [player (conj (vals (:enemies @state)) (:player @state))
            bullet (:bullets @state)]
      (when (and (< (distance player bullet) 40)
                 (> (:hp player) 0))
        (if (= player (:player @state))
          ; if it's me
          (swap! state assoc-in [:player :hp]
                 (- (:hp (:player @state))
                    (:damage bullet)))
          ; if it's enemy
          (swap! state assoc-in [:enemies (:id player) :hp]
                 (- (:hp (get (:enemies @state) (:id player)))
                    (:damage bullet))))
        (swap! state assoc :bullets
               (remove #(= % bullet) (:bullets @state)))))
    state))
