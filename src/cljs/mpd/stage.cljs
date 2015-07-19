(ns mpd.client.stage
  (:require [mpd.client.map :as map]))

(def stage (js/PIXI.Container.))
(def renderer (js/PIXI.autoDetectRenderer
                (.-innerWidth js/window) (.-innerHeight js/window)))
(set! (.-backgroundColor renderer) "0xFFFFFF")

(def stats (js/Stats.))
(set! (-> (.-domElement stats) .-style .-position) "absolute")
(set! (-> (.-domElement stats) .-style .-display) "inline-block")
(set! (-> (.-domElement stats) .-style .-top) "0px")
(set! (-> (.-domElement stats) .-style .-right) "0px")

(.appendChild (.-body js/document) (.-view renderer))
(.appendChild (.-body js/document) (.-domElement stats))

(.addChild stage map/container)

(defn render-stage []
  (js/requestAnimationFrame render-stage)
  (.begin stats)
  (update-world)
  (.end stats)
  (.render renderer stage))

(defn update-world [])
