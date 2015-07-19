(ns mpd.client.main
  (:require [mpd.client.stage :as stage]))

(defn log [& items]
  (.log js/console (apply str items)))

(js/requestAnimationFrame stage/render-stage)
