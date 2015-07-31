(ns mpd.bullets
  (:require [mpd.shared :refer [log entity_looper]]
            [mpd.assets :as assets]))

(def fire_queue (atom []))
(defn fire [bullet]
  (.stop assets/fire_sound)
  (.play assets/fire_sound)
  (swap! fire_queue conj bullet))

(defn setup []
  (log "  bullet")
  (fn [state]
    (swap! state assoc :bullets
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
              (merge bullet {:x (if (not= dx 0) (entity_looper (+ x dx)) x)
                             :y (if (not= dy 0) (entity_looper (+ y dy)) y)
                             :distance (- distance 1)})))))
                         (:bullets @state))))
    ; test for hits?
    ; create new bullets?
    (swap! state assoc :bullets
      (concat (:bullets @state) @fire_queue))
    (reset! fire_queue [])
    state))
