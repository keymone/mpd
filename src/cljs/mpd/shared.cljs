(ns mpd.shared)

(defn log [& items]
  (.log js/console (apply str items)))
