(ns mpd.shared)

(defn log [& items]
  (.log js/console (apply str items)))

(defn entity_looper [coordinate]
  (if (> coordinate 5000)
    0 (if (< coordinate 0) 5000 coordinate)))
