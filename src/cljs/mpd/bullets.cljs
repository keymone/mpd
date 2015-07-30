(ns mpd.bullets
  (:require [mpd.shared :refer [log]]))

(defn setup []
  (log "  bullet")
  (fn [state]
    (doseq [bullet (:bullets @state)]
      (let [x (:x bullet)
            y (:y bullet)
            distance (:distance bullet)]
        (if (= distance 0)
          (remove bullets bullet)
          (let [speed (:speed bullet)
                angle (:angle bullet)
                dx (* (Math/cos angle) speed)
                dy (* (Math/sin angle) speed)]
            ;(when (not (= dx 0)) (swap! state assoc-in [:bullet :x] (+ x dx)))
            ;(when (not (= dy 0)) (swap! state assoc-in [:bullet :y] (+ y dy)))
            ;(aset bullet :distance (- distance 1))
            ))))
    state))
