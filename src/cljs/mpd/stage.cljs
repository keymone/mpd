(ns mpd.stage
  (:require [mpd.shared :refer [log]]
            [mpd.stage.map :as map]))

; state - hierarchical primitive-only representation
; of current frame:
;
; [ [ <entity> <params> [ <children> ] ],
;   [ ... ] ]
;
(defn state-to-pixi [state]
  state)

(defn setup []
  (log "  stage")
  (let [renderer (js/PIXI.autoDetectRenderer
                   (.-innerWidth js/window)
                   (.-innerHeight js/window)
                   {:antialiasing false
                    :transparent false
                    :resolution 1})
        stats (js/Stats.)
        stage (js/PIXI.Container.)]
    (set! (.-backgroundColor renderer) "0xFFFFFF")
    (.appendChild (.-body js/document) (.-view renderer))

    (.addChild stage map/container)

    (fn [state]
      (.render renderer (state-to-pixi stage)) ; use % here to pass game state
      state)))
