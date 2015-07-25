(ns mpd.stage
  (:require [mpd.shared :refer [log]]))

; state - hierarchical primitive-only representation
; of current frame:
;
; [ [ <entity> <params> [ <children> ] ],
;   [ ... ] ]
;
(defn state-to-pixi [state]
  (let [world (js/PIXI.Container.)]
    (doseq [kv @state]
      (.addChild world (pixi (first kv) (last kv))))
    world))

(defmulti pixi (fn [x y] x))
(defmethod pixi :player [_ state]
  (let [obj (js/PIXI.Text. (str (:hp state)) (js-obj "fill" "red"))]
    (aset obj "anchor" (js-obj "x" 0 "y" 0))
    (aset obj "position" (js-obj "x" (:x state) "y" (:y state)))
    obj))

(defn setup []
  (log "  stage")
  (let [renderer (js/PIXI.autoDetectRenderer
                   (.-innerWidth js/window)
                   (.-innerHeight js/window)
                   #js {:antialiasing false
                        :transparent false
                        :resolution 1})]
    (set! (.-backgroundColor renderer) "0xFFFFFF")
    (.appendChild (.-body js/document) (.-view renderer))

    (fn [state]
      (.render renderer (state-to-pixi state))
      state)))
