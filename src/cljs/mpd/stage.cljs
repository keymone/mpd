(ns mpd.client.stage
  (:require [mpd.client.map :as map]
            [mpd.client.world :as world]))

(def renderer (js/PIXI.autoDetectRenderer
                (.-innerWidth js/window)
                (.-innerHeight js/window)
                {:antialiasing false
                 :transparent false
                 :resolution 1}))
(set! (.-backgroundColor renderer) "0xFFFFFF")
(.appendChild (.-body js/document) (.-view renderer))

(def stats (js/Stats.))
(set! (-> (.-domElement stats) .-style .-position) "absolute")
(set! (-> (.-domElement stats) .-style .-display) "inline-block")
(set! (-> (.-domElement stats) .-style .-top) "0px")
(set! (-> (.-domElement stats) .-style .-right) "0px")
(.appendChild (.-body js/document) (.-domElement stats))

(def stage (js/PIXI.Container.))
(.addChild stage map/container)

(defn render-stage []
  (js/requestAnimationFrame render-stage)
  (.begin stats)
  (.render renderer (world/update stage))
  (.end stats))
