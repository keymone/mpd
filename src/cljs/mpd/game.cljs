(ns mpd.game
  (:require [mpd.shared :refer [log]]))

(def state (atom {}))

(defn setup [stage player network]
  (log "  game")
  (let [stats (js/Stats.)]
    (set! (-> (.-domElement stats) .-style .-position) "absolute")
    (set! (-> (.-domElement stats) .-style .-display) "inline-block")
    (set! (-> (.-domElement stats) .-style .-top) "0px")
    (set! (-> (.-domElement stats) .-style .-right) "0px")
    (.appendChild (.-body js/document) (.-domElement stats))

    (defn gameloop []
      (js/requestAnimationFrame gameloop)
      (.begin stats)
      (-> state network player stage)
      (.end stats)))

    (js/requestAnimationFrame gameloop))
