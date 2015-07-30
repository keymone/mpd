(ns mpd.game
  (:require [mpd.shared :refer [log]]))

(def state (atom {
  :player {:id -1
           :x 300
           :y 300
           :hp 100
           :speed 3
           :rotation 0
           :primary false
           :secondary false}
  :enemies {
    42 {:id -1
        :x 300
        :y 300
        :hp 100
        :speed 3
        :rotation 10
        :primary false
        :secondary false}}
}))

(defn setup [stage player network]
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
      (-> state network player stage)
      (.end stats))
    (js/requestAnimationFrame gameloop)))
