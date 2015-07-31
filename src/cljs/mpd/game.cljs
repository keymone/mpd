(ns mpd.game
  (:require [mpd.shared :refer [log]]))

(def state (atom {
  :player {:id -1
           :x (+ (rand-int 300) 100)
           :y (+ (rand-int 300) 100)
           :hp 100
           :speed 3
           :rotation 0
           :primary false
           :secondary false
           :type "character"}
  :enemies {}
  :bullets []
  :crosshair {:x 0
              :y 0}}))

(defn setup [stage player network bullets collisions enemies]
  (log "  game")
  (let [stats (js/Stats.)]
    (set! (-> (.-domElement stats) .-style .-position) "absolute")
    (set! (-> (.-domElement stats) .-style .-display) "inline-block")
    (set! (-> (.-domElement stats) .-style .-top) "0px")
    (set! (-> (.-domElement stats) .-style .-right) "0px")
    (.appendChild (.-body js/document) (.-domElement stats))

    (.addEventListener js/window "contextmenu" #(do (.preventDefault %) false))

    (defn gameloop []
      (js/requestAnimationFrame gameloop)
      (.begin stats)
      (-> state network player enemies bullets collisions stage)
      (.end stats))
    (js/requestAnimationFrame gameloop)))
