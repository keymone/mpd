(ns mpd.shared)

(defn log [& items]
  (.log js/console (apply str items)))

(defn entity_looper [coordinate]
  (if (> coordinate 5000)
      (- coordinate 5000)
      (if (< coordinate 0)
          (+ coordinate 5000)
          coordinate)))
