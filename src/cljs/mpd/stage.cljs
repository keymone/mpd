(ns mpd.stage
  (:require [mpd.shared :refer [log]]))

(defmulti pixi (fn [x y] x))
(defmethod pixi :player [_ state]
  (let [fill (case [(:primary state) (:secondary state)]
               [true true] "green"
               [true false] "red"
               [false true] "blue"
               "black")
        obj (js/PIXI.Text.
              (str (:id state))
              (js-obj "fill" fill))]
    (aset obj "anchor" (js-obj "x" 0.5 "y" 0.5))
    (aset obj "position" (js-obj "x" (:x state) "y" (:y state)))
    (aset obj "rotation" (:rotation state))
    obj))

; state - hierarchical primitive-only representation
; of current frame:
;
; [ [ <entity> <params> [ <children> ] ],
;   [ ... ] ]
;
(defn state-to-pixi [state]
  (let [world (js/PIXI.Container.)]
    (doseq [kv @state]
      (.addChild world (apply pixi kv)))
    world))

(defn setup []
  (log "  stage")
  (let [renderer (js/PIXI.autoDetectRenderer
                   (- (.-innerWidth js/window) 4)
                   (- (.-innerHeight js/window) 4)
                   #js {:antialiasing false
                        :transparent false
                        :resolution 1})]
    (set! (.-backgroundColor renderer) "0xFFFFFF")
    (.appendChild (.-body js/document) (.-view renderer))
    (fn [state] (.render renderer (state-to-pixi state)))))
