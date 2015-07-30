(ns mpd.stage
  (:require [mpd.shared :refer [log]]))

(defmulti pixi (fn [x y] x))
(defmethod pixi :player [_ state]
  (let [fill (case [(:primary state) (:secondary state)]
               [true true] "green"
               [true false] "red"
               [false true] "blue"
               "green")
        obj (js/PIXI.Text.
              (str (:id state))
              (js-obj "fill" fill))]
    (aset obj "anchor" (js-obj "x" 0.5 "y" 0.5))
    (aset obj "position" (js-obj "x" (:x state) "y" (:y state)))
    (aset obj "rotation" (:rotation state))
    [obj]))

(defmethod pixi :enemies [_ enemies]
  (reduce (fn [agg enemy]
            (concat agg (pixi :player (last enemy))))
          [] (seq enemies)))

(defmethod pixi :bullet [_ bullet]
  (let [shape (js/PIXI.Text.
                (str "b")
                (js-obj "fill" "red"))]
    (aset shape "anchor" (js-obj "x" 0.5 "y" 0.5))
    (aset shape "position" (js-obj "x" (:x bullet) "y" (:y bullet)))
    [shape]))

(defmethod pixi :bullets [_ bullets]
  (reduce (fn [agg bullet]
            (concat agg (pixi :bullet bullet)))
          [] (seq bullets)))

(def world (js/PIXI.Container.))
(def background_sprite
  (let [bg_sprite (PIXI.extras.TilingSprite.fromImage "images/floor.png" 10000 10000)]
    (aset bg_sprite "scale" (js-obj "x" 0.5 "y" 0.5))
    bg_sprite))

(defn state-to-pixi [state]
  (.removeChildren world)
  (.addChild world background_sprite)
  (doseq [kv @state]
    (doseq [obj (apply pixi kv)]
      (.addChild world obj)))
  world)

(defn setup []
  (log "  stage")
  (let [renderer (js/PIXI.autoDetectRenderer
          (- (.-innerWidth js/window) 4)
          (- (.-innerHeight js/window) 4)
          #js {:antialiasing false
          :transparent false
          :resolution 1})
       ]
    (set! (.-backgroundColor renderer) "0xFFFFFF")
    (.appendChild (.-body js/document) (.-view renderer))
    ;(.generateTexture world renderer)
    (fn [state] (.render renderer (state-to-pixi state)))))
