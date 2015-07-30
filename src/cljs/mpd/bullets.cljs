(ns mpd.bullets
  (:require [mpd.shared :refer [log]]))

(defn setup []
  (log "  bullet")
  (fn [state]
    (swap!
      state assoc :bullets
      (remove nil? (mapv (fn [bullet]
        (let [x (:x bullet)
              y (:y bullet)
              distance (:distance bullet)]
          (if (= distance 0)
            nil
            (let [speed (:speed bullet)
                  angle (:angle bullet)
                  dx (* (Math/cos angle) speed)
                  dy (* (Math/sin angle) speed)]
              (merge bullet {:x (if (not= dx 0) (+ x dx) x)
                             :y (if (not= dy 0) (+ y dy) y)
                             :distance (- distance 1)})))))
                         (:bullets @state))))
    state))
