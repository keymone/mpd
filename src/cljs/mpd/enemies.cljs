(ns mpd.enemies
  (:require [mpd.shared :refer [log]]
            [mpd.bullets :as bullets]))

(def enemies (atom {}))

(defn sync [json]
  (let [data (js->clj json :keywordize-keys true)
        entities (:entities data)]
    (when (> (count entities) 0) (doseq [e entities] (bullets/fire e)))
    (swap! enemies conj {(:id data) data})))

(defn setup []
  (log "  enemies")
  (fn [state]
    (swap! state assoc :enemies @enemies)
    state))
